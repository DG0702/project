let isIdDuplication = false;

async function checkIdValidation() {


    let userId = document.querySelector("#id").value
    let message = document.querySelector("#id-check-message")


    // 빈 칸 방지
    if(userId.trim() === ""){
        message.textContent = "아이디를 입력하세요!"
        message.style.color = "red"
        return false;
    }
    
    // 아이디(이메일) 형식 확인
    const emailPattern = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-z]{2,4}$/;

    if(!emailPattern.test(userId)){
        message.textContent = "이메일 형식이 아닙니다! (도메인은 .com만 허용됩니다.)"
        message.style.color = "red"
        return false;
    }else if(userId.length > 15){
        message.textContent = "이메일은 최대 15자까지 입력 가능합니다"
        message.style.color = "red"
        return false;
    }


    // encodeURIComponent(userId)을 사용해 입력될 userId값 중 특수문자 포함시 특수문자를 인코딩 하여 전달할 목적으로 사용
    // fetch 메서드를 활용하여 GET 요청으로 URL에 비동기 실행
    // 중복 확인
    try {
        // 기본 url : "/id-check-duplicate?user_id="
        // (Get,Post 등) Mapping 할 경우 url : "/id-check-duplicate"
        // 쿼리 파라미터 :  + encodeURIComponent(userId)
        // @RequestParam 사용
        const response = await fetch("/id-check-duplicate?user_id=" + encodeURIComponent(userId), {
            method: "GET"
        })
        if (!response.ok) {
            alert("서버 오류 발생")
            return false;
        }


        const result = await response.json();

        if (result.isDuplicate) {
            message.textContent = "이미 사용중인 아이디입니다"
            message.style.color = "red"
            isIdDuplication = false;
            return false;

        } else {
            message.textContent = "사용 가능한 아이디입니다."
            message.style.color = "green"
            isIdDuplication = true;
            return true;
        }
    } catch (error) {
        alert("에러가 발생했습니다." + error.message)
        isIdDuplication = false;
        return false;
    }

}

function checkPwValidation(){
    let pw = document.querySelector("#pw").value
    let message = document.querySelector("#pw-check-message")


    if(pw.trim() === ""){
        message.textContent = "비밀번호를 입력해주세요!";
        message.style.color = "red";
        return false
    }else if(pw.length < 4 || pw.length > 20){
        message.textContent = "비밀번호는 최소 4자리 이상 최대 20자리 이하 입력가능합니다!";
        message.style.color = "red";
        return false
    }else{
        message.textContent = "비밀번호가 확인 되었습니다";
        message.style.color = "green";
        return true;
    }

}

function checkNameValidation() {
    let name = document.querySelector("#name").value
    let message = document.querySelector("#name-check-message")

    const namePattern =  /^[A-Za-z가-힣]+$/;

    if(name.trim() === ""){
        message.textContent = "이름을 입력해주세요!";
        message.style.color = "red";
        return false
    }else if(name.split(" ").length > 1){
        message.textContent = "이름에 공백이 들어갈 수 없습니다!";
        message.style.color = "red";
        return false
    }
    else if(!namePattern.test(name)) {
        message.textContent = "이름은 알파벳과 한글만 포함할 수 있습니다!";
        message.style.color = "red";
        return false
    }else if(!namePattern.test(name).length > 20){
        message.textContent = "이름은 최대 20자리까지 입력 가능합니다.";
        message.style.color = "red";
        return false
    }else{
        message.textContent = "이름이 확인 되었습니다";
        message.style.color = "green";
        return true;
    }
}

let isPhoneDuplication = false;

async function checkPhoneValidation() {
    let Phone = document.querySelector("#phone").value
    let message = document.querySelector("#Phone-check-message")


    // 전화번호 형식
    const tel = /^010-\d{4}-\d{4}$/

    // 빈 칸 방지
    if(Phone.trim() === ""){
        message.textContent = "전화번호를 입력해주세요!"
        message.style.color = "red"
        return false
    }
    // 전화번호 형식 확인
    else if(!tel.test(Phone)){
        message.innerHTML = "전화번호 형식이 올바르지 않습니다! <br> 올바른 형식 : 010-xxxx-xxxx"
        message.style.color = "red"
        return false
    }

    // 중복확인 
    try {
        const response = await fetch("/Phone-check-duplicate?phone_number=" + encodeURIComponent(Phone), {
            method: "GET"
        })
        if (!response.ok) {
            alert("서버 오류 발생");
            return false;
        }

        const result = await response.json();

        if (result.isDuplicate) {
            message.textContent = "이미 사용중인 전화번호입니다."
            message.style.color = "red"
            isPhoneDuplication = false;
            return false;
        } else {
            message.textContent = "사용 가능한 전화번호입니다"
            message.style.color = "green"
            isPhoneDuplication = true;
            return true;
        }
    } catch (error) {
        alert("에러가 발생했습니다." + error.message)
        isPhoneDuplication = false;
        return false;
    }
}



let isNickNameDuplication = false;

async function checkNicknameValidation(){
    let nickName = document.querySelector("#nickname").value
    let message = document.querySelector("#nickname-check-message")

    
    // 빈 칸 방지
    if(nickName.trim() === ""){
        message.textContent = "닉네임을 입력해주세요!"
        message.style.color = "red"
        return false;
    }

    // 글자 수 제한
    if(nickName.length > 20){
        message.textContent = "닉네임은 20자 이내로 입력해주세요!"
        message.style.color = "red"
        return false;
    }
    
    // 중복 확인
    try {
        const response =await fetch("/nick-check-duplicate?nick_name=" + encodeURIComponent(nickName), {
            method: "GET"
        })
        if(!response.ok){
            alert("서버 오류 발생");
            return false;
        }

        const result = await response.json();

        if (result.isDuplicate) {
            message.textContent = "이미 사용중인 닉네임입니다"
            message.style.color = "red"
            isNickNameDuplication = false
            return false;
        } else {
            message.textContent = "사용 가능한 닉네임입니다"
            message.style.color = "green"
            isNickNameDuplication = true;
            return true;
        }

    } catch (error) {
        alert("에러가 발생했습니다" + error.message)
        isNickNameDuplication = false;
        return false;
    }

}

let isAddrCheck = false;

function checkAddrValidation() {
    let addr = document.querySelector("#addr").value
    let message = document.querySelector("#addr-check-message")

    
    // 빈 칸 방지
    if(addr.trim() === ""){
        message.textContent = "주소를 입력해주세요!"
        message.style.color = "red"
        return false;
    }

    
    // 주소 형식 확인
    const addrPattern = /^(서울|제주도)(특별시)-([가-힣]+구)-([가-힣]+동)$|^(울산|대구|부산|광주|대전|인천)(광역시)-([가-힣]+구)-([가-힣]+동)$|^(경상남도|경상북도|전라남도|전라북도|충청남도|충청북도|경기도|강원도)-([가-힣]+시)-([가-힣]+동)$|^(경상남도|경상북도|전라남도|전라북도|충청남도|충청북도|경기도|강원도)-([가-힣]+시)-([가-힣]+구)?-([가-힣]+동)$/

    if(!addrPattern.test(addr)){
        message.innerHTML = "주소 형식이 올바르지 못합니다 <br> 올바른 형식 : 서울특별시-강남구-역삼동 <br> 올바른 형식 : 경기도-수원시-인계동"
        message.style.color = "red"
        return false;
        }else{
        message.textContent = "주소 확인 되었습니다"
        message.style.color = "green"
        isAddrCheck = true;
        return true;
    }
}

let isBirthCheck = false;

function checkBirthValidation() {
    let birth = document.querySelector("#birth").value
    let message = document.querySelector("#birth-check-message")

    
    // 빈 칸 방지
    if(birth.trim() === ""){
        message.textContent = "생일을 입력해주세요!"
        message.style.color = "red"
        return false;
    }

    // 생일 형식 확인
    const birthPattern = /^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/;

    if(!birthPattern.test(birth)){
        message.innerHTML = "생일 형식이 올바르지 못합니다. <br> 올바른 형식 : 2000-01-01"
        message.style.color = "red"
        return false;
    }else{
        // 입력 받은 birth 유효성 검사 >> 날짜가 있는지 없는지 확인 하는 코드

        const[year,month,day] = birth.split("-").map(Number)
        // split("구분자") : 문자열을 구분자 기준으로 나누어 배열 생성을 하고
        // map(Number) : 배열의 요소에 Number 함수를 적용하여 값을 문자열에서 숫자로 변환해준다


        const date = new Date(year,month-1,day);
        // date 인스턴스 객체 생성 후 Date객체에 메서드 사용
        const valid= date.getFullYear() === year && date.getMonth() === month-1 && date.getDate() === day;
        // valid 변수는 Boolean 자료형 값으로 true, false를 반환

        // 유효한 날짜인지 확인 후 결과 알림
        if(valid){
            message.textContent = "생년월일이 확인 되었습니다."
            message.style.color = "green"
            isBirthCheck = true;
            return true;
        }else{
            message.textContent = "유효하지 않은 생년월일입니다."
            message.style.color = "red"
            return false
        }
    }
}

function checkGenderValidation() {
    let gender = document.querySelector(`input[name="gender"]:checked`);
    let message = document.querySelector("#gender-check-message")

    if(!gender){
        message.textContent = "(남, 여, 선택안함) 중 하나를 택 해주세요!";
        message.style.color = "red";
        return false
    }else{
        message.textContent = "성별이 확인 되었습니다.";
        message.style.color = "green";
        return  true;
    }
}

async function checkAllValidations() {
    let isFormValid = true;
    let errorMessage = [];

    if(!isIdDuplication || !await checkIdValidation()){
        isFormValid = false;
        errorMessage.push("아이디")
    }

    if(!checkPwValidation()){
        isFormValid = false
        errorMessage.push("비밀번호")
    }
    if(!checkNameValidation()){
        isFormValid = false
        errorMessage.push("이름")
    }
    if(!isPhoneDuplication || !await checkPhoneValidation()){
        isFormValid = false
        errorMessage.push("전화번호")
    }
    
    if(!isNickNameDuplication || !await checkNicknameValidation()){
        isFormValid = false
        errorMessage.push("닉네임")
    }

    if(!isAddrCheck || !checkAddrValidation()){
        isFormValid = false
        errorMessage.push("주소")
    }
    if(!isBirthCheck || !checkBirthValidation()){
        isFormValid = false
        errorMessage.push("생년월일")
    }
    if(!checkGenderValidation()){
        isFormValid = false
        errorMessage.push("성별")
    }

    return {isFormValid,errorMessage};
}

async  function submitForm() {
    let check = document.querySelector("#flexCheckDefault")

    if(!check.checked){
        alert("약관에 동의해야 합니다.")
        return false;
    }

    const ValidationResult = await checkAllValidations();

    if(ValidationResult.isFormValid){
        document.querySelector("#join").submit()
        alert("회원가입에 성공하셨습니다")
    }else{
        alert("다음 항목을 확인해주세요 ! : " + ValidationResult.errorMessage.join(" , "));
    }

    return ValidationResult.isFormValid;
}

