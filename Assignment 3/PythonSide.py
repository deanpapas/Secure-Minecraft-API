import hashlib
import hmac
from Crypto.Cipher import AES  # pycryptodome package
import base64
import os

### Keys ###
ekey = "B&E)H@McQeThWmZq"
mkey = b"UTYn4JIOaz" 
message = b"chat.post(hello)"

print("=============================")

### Encryption ###
print("Encrypting: "+ str(message))
iv = os.urandom(AES.block_size)
cipher = AES.new(ekey.encode("UTF-8"), AES.MODE_CBC, iv)

BLOCK_SIZE = 16
length = BLOCK_SIZE - len(message) % BLOCK_SIZE
padding = bytes([length]) * length

encrypted = cipher.encrypt(message + padding)
ciphertext = base64.b64encode(iv+encrypted)
print("Encrypted Message: " + ciphertext.decode())

### MAC ###
mac = hmac.new(mkey, ciphertext, hashlib.sha256).hexdigest()
print("Generated MAC in Python: ", mac)

### Prepare Message ###
prepared_message = ciphertext + b"," + mac.encode() + b"\n"
print("Prepared Message: " + str(prepared_message))

print("=============================")




