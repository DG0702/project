// 아이디, 비밀번호 확인 폼 제출
document.querySelector("#login-section").addEventListener("submit",function (e){
    // 폼 제출 방지
    e.preventDefault();
    
    // 아이디, 비밀번호 값
    const userId = document.querySelector("#id").value
    const password = document.querySelector("#pw").value

    // 아이디, 비밀번호 메시지
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

    // 폼 유효성 체크 변수 (아이디, 비밀번호가 동시에 빈칸 일 경우 2개의 메시지 동시에 표현)
     let isValid = true;

    // userId == null 사용 불가

    // 아아디 빈칸일 때
    if(userId.trim() === ""){
        id_message.textContent = "아이디를 입력해주세요! ";
        id_message.style.color = "red";
        isValid = false;
    }

    // 비밀번호 빈칸일 때
    if(password.trim() === ""){
        pw_message.textContent = "비밀번호를 입력해주세요! ";
        pw_message.style.color = "red";
        isValid = false;
    }
    
    // 유효성 검사 완료 되었을 경우
    if (isValid) {
        // 비동기 실행
        try {
            fetch("/validUser", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({userId, password})
            })
                // 응답 데이터
                .then(response => {
                    return response.json()
                })
                .then(data => {
                    if (data.success) {
                        // 회원정보 띄위기
                        document.querySelector("#edit-section").style.display = "block"
                        // 회원정보 확인 지우기
                        document.querySelector("#login-section").style.display = "none"
                        // 회원정보 가져오기
                        fetch(`/getUserInfo?userId=${data.userId}`)
                            .then(response => {
                                if (!response.ok) {
                                    // response.statusText : Not Found
                                    throw new Error("서버 오류 " + response.statusText)
                                }
                                return response.json()
                            })
                            .then(userData => {
                                document.querySelector("#data-id").value = userData.userId
                                document.querySelector("#edit-pw").value = userData.password
                                document.querySelector("#edit-phone").value = userData.phone
                                document.querySelector("#edit-addr").value = userData.addr

                                if (userData.gender === "M") {
                                    document.querySelector("#btnradio1").checked = true
                                }
                                if (userData.gender === "F") {
                                    document.querySelector("#btnradio2").checked = true
                                }
                                if (userData.gender === "선택안함") {
                                    document.querySelector("#btnradio3").checked = true
                                }
                            })
                            .catch(error => {
                                alert("회원정보를 가져오지 못했습니다. " + error.message)
                            })
                    } else {
                        if(data.idValid === false){
                            alert("아이디가 존재하지 않습니다.")
                        }else if(data.pwValid === false){
                            alert("비밀번호가 틀렸습니다.")
                        }
                    }
                })
                .catch(error => {
                    alert("서버 오류 발생 " + error.message)
                })
        } catch (error) {
            alert("예상치 못한 오류 발생 " + error.message)
        }
    }

})