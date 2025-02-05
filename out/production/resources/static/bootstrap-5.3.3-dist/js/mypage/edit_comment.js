// 폼 제출 시
document.querySelector("#comment-form").addEventListener("submit",function (e) {
    // 폼 제출 방지
    e.preventDefault()

    // 폼 데이터 가져오기 -> FormData 객체는 JSON.stringify() 불가능
    const formData = new FormData(this)


    // JSON.stringify() 가능한 객체를 별도로 생성
    const formObject = {};

    formData.forEach((value, key) => {
        formObject[key] = value
    })



    // 댓글 아이디
    const commentId = document.querySelector("#commentId").value

    try{
        fetch(`/comments/` + commentId,{
            method : "PATCH",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify(formObject)
        })
            .then(response => {
                if(!response.ok){
                    throw new Error("오류 발생")
                }
                return response.json()
            })
            .then(data =>{
                if(data.success){
                    alert("댓글을 수정 하였습니다")
                    window.location.href = `/users/comments`
                }else{
                    alert("댓글 수정을 실패 하였습니다.")
                }
            })
            .catch(error =>{
                alert("댓글 수정 오류 발생 " + error.message)
            })
    }
    catch(error){
        alert("예상치 못한 오류 발생 " + error.message)
    }

})