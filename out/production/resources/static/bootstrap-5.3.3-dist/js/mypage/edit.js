// 수정 폼 제출
document.querySelector("#edit-form").addEventListener("submit",function (e) {
    // 폼 제출 방지
    e.preventDefault()

    // 회원 정보 기본값
    const userId = document.querySelector("#data-id").value;

    // 회원 정보 수정한 값
    const password = document.querySelector("#edit-pw").value
    const phoneNumber = document.querySelector("#edit-phone").value
    const addr = document.querySelector("#edit-addr").value
    const genderElement = document.querySelector(`input[name="gender"]:checked`)
    const gender = genderElement?genderElement.value:null;

    // 오류메시지
    let pw_message = document.querySelector("#edit-pw-message");
    let phone_message = document.querySelector("#edit-phone-message");
    let addr_message = document.querySelector("#edit-addr-message");

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

    // 유효성검사
    let isValid = true;

    // 전화번호 형식
    const tel = /^010-\d{4}-\d{4}$/

    // 비밀번호 빈칸일 경우
    if(password.trim() === ""){
        pw_message.textContent = "비밀번호를 입력해주세요!"
        pw_message.style.color = "red"
        isValid = false;
    }
    // 비밀번호 자리 수 (4~20)
    else if(password.length < 4 || password.length > 20){
        pw_message.textContent = "비밀번호는 최소 4자리 최대 20자리 이하 입력가능합니다!"
        pw_message.style.color = "red"
        isValid = false;
    }

    // 전화번호 빈칸일 경우
    if(phoneNumber.trim() === ""){
        phone_message.textContent = "전화번호를 입력해주세요!"
        phone_message.style.color = "red"
        isValid = false;
    }
    // 전화번호 형식이 다를 경우
    else if(!tel.test(phoneNumber)){
        phone_message.innerHTML = "전화번호 형식이 올바르지 않습니다. <br> 올바른 형식 : 010-xxxx-xxxx"
        phone_message.style.color = "red"
        isValid = false
    }

    // 주소가 빈칸일 경우
    if(addr.trim() === ""){
        addr_message.textContent = "주소를 입력해주세요!"
        addr_message.style.color = "red";
        isValid = false;
    }

    if (isValid) {
        try {
            fetch(`/check-Phone-Duplicate?phoneNumber=${encodeURIComponent(phoneNumber)}`)
                .then(response => response.json())
                .then(data =>{
                    if (data.isDuplicate) {
                        phone_message.textContent = "이미 사용중인 전화번호입니다."
                        phone_message.style.color = "red"
                        return false;
                    } else {
                        fetch("/users/edit", {
                            method: "PUT",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify({userId, password, phoneNumber, addr, gender})
                        })
                            .then(response => {
                                return response.json()
                            })
                            .then(data => {
                                if (data.success) {
                                    alert("회원 정보가 수정되었습니다")
                                    window.location.href = "/users/mypage"
                                } else {
                                    alert(data.message)
                                }
                            })
                            .catch(error => {
                                alert("서버 오류 발생 " + error.message)
                            })
                    }
                })
        } catch (error) {
            alert("예상치 못한 오류 발생 " + error.message)
        }
    }
})