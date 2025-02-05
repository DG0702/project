// 아이디 유효성 검사
function check() {
    // 입력한 값 
    let id = document.querySelector("#id").value
    let pw = document.querySelector("#pw").value
    
    // 오류 메시지
    let id_message = document.querySelector("#id-message")
    let pw_message = document.querySelector("#pw-message")

    // 오류메시지 초기화
    document.querySelectorAll(".input").forEach(function(inputElement) {
        inputElement.addEventListener("input", function () {
            // 각 입력 필드에 해당하는 메시지 요소를 찾기
            const messageElement = document.querySelector(`#${inputElement.id}-message`);

            if (messageElement) {
                // 오류 메시지 초기화
                messageElement.textContent = "";
                messageElement.style.color = "";
            }
        });
    });
    
    // 폼 제출 유효성 검사 결과 값 (아이디,비밀번호 메시지 동시 표현 방법)
    let isValue = true;
    
    // 아이디가 빈칸 일 경우
    if(id.trim() === ""){
        id_message.textContent = "아이디를 입력해주세요!"
        id_message.style.color = "red"
        isValue = false;
    }
    
    // 비밀번호가 빈칸 일 경우
    if(pw.trim() === ""){
        pw_message.textContent = "비밀번호를 입력해주세요!"
        pw_message.style.color = "red"
        isValue = false;
    }
    // 반환 값
    return isValue;
}