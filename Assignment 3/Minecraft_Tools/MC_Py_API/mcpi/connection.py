import socket
import select
import sys
from .util import flatten_parameters_to_bytestring

### Encryption Imports ###
from Crypto.Cipher import AES  # pycryptodome package
import base64
import os

## MAC Imports ##
import hmac
import hashlib

""" @author: Aron Nieminen, Mojang AB"""


class RequestError(Exception):
    pass


class Connection:
    """Connection to a Minecraft Pi game"""
    RequestFailed = "Fail"

    def __init__(self, address, port):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.connect((address, port))
        self.lastSent = ""

    def drain(self):
        """Drains the socket of incoming data"""
        while True:
            readable, _, _ = select.select([self.socket], [], [], 0.0)
            if not readable:
                break
            data = self.socket.recv(1500)
            e = "Drained Data: <%s>\n" % data.strip()
            e += "Last Message: <%s>\n" % self.lastSent.strip()
            sys.stderr.write(e)

    def send(self, f, *data):
        """
        Sends data. Note that a trailing newline '\n' is added here

        The protocol uses CP437 encoding - https://en.wikipedia.org/wiki/Code_page_437
        which is mildly distressing as it can't encode all of Unicode.
        """
        ### Keys ###
        ekey = "B&E)H@McQeThWmZq"
        mkey = b"UTYn4JIOaz" 

        ### Prepare Input For Encryption ###
        s = b"".join([f, b"(", flatten_parameters_to_bytestring(data), b")"])
        
        ### Encryption ###
        print("=============================")
        print("Encrypting: "+ str(s))
        iv = os.urandom(AES.block_size)
        cipher = AES.new(ekey.encode("UTF-8"), AES.MODE_CBC, iv)

        BLOCK_SIZE = 16
        length = BLOCK_SIZE - len(s) % BLOCK_SIZE
        padding = bytes([length]) * length

        encrypted = cipher.encrypt(s + padding)
        ciphertext = base64.b64encode(iv+encrypted)
        print("Encrypted Message: " + ciphertext.decode())

        ### MAC ###
        mac = hmac.new(mkey, ciphertext, hashlib.sha256).hexdigest()
        print("Generated MAC in Python: ", mac)

        ### Prepare Message ###
        prepared_message = ciphertext + b"," + mac.encode() + b"\n"
        print("Prepared Message: " + str(prepared_message))
        print("=============================")

        ### Send Message ###
        self._send(prepared_message)


    def _send(self, s):
        """
        The actual socket interaction from self.send, extracted for easier mocking
        and testing
        """
        self.drain()
        self.lastSent = s

        self.socket.sendall(s)

    def receive(self):
        """Receives data. Note that the trailing newline '\n' is trimmed"""
        s = self.socket.makefile("r").readline().rstrip("\n")
        if s == Connection.RequestFailed:
            print("Request Failed!")
            #raise RequestError("%s failed"%self.lastSent.strip())
        return s

    def sendReceive(self, *data):
        """Sends and receive data"""
        self.send(*data)
        return self.receive()
