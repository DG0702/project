// 아이디 조회 버튼 클릭시 발생하는 이벤트
document.querySelector("#findIdForm").addEventListener("submit", function (e){
    // 폼 제출 제한
    e.preventDefault();

    // 입력한 값
    const name = document.querySelector("#name").value
    const phone = document.querySelector("#phone").value

    // 메시지 기본값
    const name_message = document.querySelector("#name-message")
    const phone_message = document.querySelector("#phone-message")

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



    // 전화번호 형식
    const tel = /^010-\d{4}-\d{4}$/

    // 유효성 검사
    let isValue = true;

    // 이름이 빈칸일 경우
    if(name.trim() === ""){
        name_message.textContent = "이름을 입력해주세요!"
        name_message.style.color = "red"
        isValue = false;
    }

    // 전화번호가 빈칸일 경우
    if(phone.trim() === ""){
        phone_message.textContent = "전화번호를 입력해주세요!"
        phone_message.style.color = "red"
        isValue = false;
    }
    // 전화번호 형식이 다를 경우
    else if(!tel.test(phone)){
        phone_message.innerHTML = "전화번호 형식이 올바르지 않습니다. <br> 올바른 형식 : 010-xxxx-xxxx"
        phone_message.style.color = "red"
        isValue = false;
    }

    // 유효성 검사
    if (isValue) {
        try {
            // 비동기 실행
            fetch("/find-id", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({name, phone})
            })
                // 서버에서 응답
                .then(response => response.json())
                // 응답 받은 값 활용
                .then(data => {
                    const modal = document.querySelector("#modal-body")
                    if (data.success) {
                        modal.innerHTML = `<p style='text-align: center; font-weight : 700; font-size: 20px;'>귀하의 아이디는  ${data.id} 입니다. </p>`
                    } else {
                        modal.innerHTML = `<p style='text-align: center; font-weight : 700; font-size: 20px;'>아이디 찾기에 실패했습니다. 다시 시도 해주세요.</p>`
                    }

                    //  모달을 Bootstrap의 Modal객체로 초기화 후 호출하여 화면에 띄우는 코드
                    new bootstrap.Modal(document.querySelector("#idModal")).show();
                })
                .catch(error => {
                    alert("서버 오류 발생 " + error.message)
                })

        } catch (error) {
            alert("예상치 못한 오류 발생 " + error.message)
        }
    }
})