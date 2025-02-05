// 비밀번호 찾기 조회 시 발생하는 이벤트
document.querySelector("#findPwForm").addEventListener("submit",function (e){
    // 폼 생성 제출 방지
    e.preventDefault();

    // 입력한 값
    const name = document.querySelector("#name").value
    const id = document.querySelector("#id").value

    // 오류메시지
    const name_message = document.querySelector("#name-message")
    const id_message = document.querySelector("#id-message")

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

    // 아이디(이메일) 형식 확인
    const emailPattern = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-z]{2,4}$/;

    // 유효성 검사
    let isValue = true;

    // 이름 빈칸일 경우
    if(name.trim() === ""){
        name_message.textContent = "이름을 입력해주세요!"
        name_message.style.color = "red"
        isValue = false;
    }

    // 아이디 빈칸일 경우
    if(id.trim() === ""){
        id_message.textContent = "아이디를 입력해주세요!"
        id_message.style.color = "red"
        isValue = false;
    }
    // 아이디(이메일) 형식
    else if(!emailPattern.test(userId)){
        message.textContent = "이메일 형식이 아닙니다! (도메인은 .com만 허용됩니다.)"
        message.style.color = "red"
        isValue = false;
    }
    // 아이디(이메일) 글자수 제한
    else if(userId.length > 15){
        message.textContent = "이메일은 최대 15자까지 입력 가능합니다"
        message.style.color = "red"
        isValue = false;
    }


    if (isValue) {
        try {
            // 비동기 실행
            fetch("find-pw", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({name: name, id: id})
            })
                // 응답 받을 데이터
                .then(response => response.json()
                )
                .then(data => {
                    const modal = document.querySelector("#modal-body")
                    if (data.success) {
                        modal.innerHTML = `<p style='text-align: center; font-weight : 700; font-size: 20px;'> 귀하의 비밀번호는 ${data.password} 입니다. </p>`
                    } else {
                        modal.innerHTML = `<p style='text-align: center; font-weight : 700; font-size: 20px;'> 비밀번호 찾기에 실패했습니다. 다시 시도 해주세요. </p>`
                    }
                    //  모달을 Bootstrap의 Modal객체로 초기화 후 호출하여 화면에 띄우는 코드
                    new bootstrap.Modal(document.querySelector("#pwModal")).show();
                })
                .catch(error => {
                    alert("서버 오류 발생 " + error.message)
                })
        } catch (error) {
            alert("예상치 못한 오류 발생 " + error.message)
        }
    }
})