function showConfirm() {
    const userConfirmed= confirm("자유게시판은 로그인 후 사용이 가능합니다. \n로그인 페이지 이동하시겠습니까?")

    if(userConfirmed){
        window.location.href ='/login-page'
    }

}