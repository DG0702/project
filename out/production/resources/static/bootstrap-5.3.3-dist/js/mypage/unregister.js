// 폼 제출시
document.querySelector("#unregister-form").addEventListener("submit",function (e) {
    // 폼 제출 방지
    e.preventDefault()

    // 아이디, 비밀번호 값
    const userId = document.querySelector("#id").value
    const password = document.querySelector("#pw").value
    const unregister = document.querySelector("#words").value

    // 오류 메시지
    let id_message = document.querySelector("#id-message");
    let pw_message = document.querySelector("#pw-message");
    let word_message = document.querySelector("#words-message")

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
    // 유효성 검사
    let isValid = true;
    
    // 아이디 빈칸일 경우
    if(userId.trim() === ""){
        id_message.textContent = "아이디를 입력해주세요.";
        id_message.style.color = "red";
        isValid = false;
    }

    // 비밀번호 빈칸일 경우
    if(password.trim() === ""){
        pw_message.textContent = "비밀번호를 입력해주세요.";
        pw_message.style.color = "red";
        isValid = false;
    }

    // 탈퇴 문구가 빈칸일 경우
    if(unregister.trim() === ""){
        word_message.textContent = "'탈퇴하겠습니다' 라는 문구를 입력해주세요!";
        word_message.style.color = "red";
        isValid = false;
    }else if(unregister.trim() !== "탈퇴하겠습니다"){
        word_message.textContent = "'탈퇴하겠습니다' 라는 문구를 정확하게 입력해주세요!";
        word_message.style.color = "red";
        isValid = false;
    }
    


    if(isValid){
        // 마지막 확인
        const isConfirm = confirm(" 회원을 탈퇴하시겠습니까? \n 회원 탈퇴 시 모든 게시물은 삭제됩니다!")
        if (isConfirm) {
            try {
                fetch("/users/unregistration",{
                    method : "POST",
                    headers : {
                        "Content-Type" : "application/json"
                    },
                    body : JSON.stringify({userId,password})
                })
                    .then(response=> response.json())
                    .then(data =>{
                        if(data.success){
                            // url : `/users/unregister/${encodeURIComponent(userId)}`
                            // 따라서 @PathVariable 사용
                            fetch(`/users/${encodeURIComponent(userId)}`, {
                                method: "DELETE"
                            })
                                .then(response => response.json())
                                .then(data => {
                                    if (data.success) {
                                        alert(data.message)
                                        window.location.href = "/"
                                    }else{
                                        alert(data.message)
                                    }
                                })
                                .catch(error =>{
                                    alert("서버 오류 " + error.message)
                                })
                        }else{
                            alert(data.message)
                        }
                    })
            } catch (error) {
                alert("예상치 못한 오류 발생 " + error.message)
            }
        }else{
            alert("회원 탈퇴를 취소하셨습니다.")
        }
    }
})