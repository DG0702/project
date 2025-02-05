// 내가 쓴 댓글페이지에서 수정 버튼을 눌렸을 경우
document.querySelector("#comment-edit-btn").addEventListener("click",function (){

    // 체크된 댓글 리스트
    const checkBoxes = document.querySelectorAll('input[name="check-num"]:checked')

    if(checkBoxes.length === 1){
        const commentId = checkBoxes[0].value
        window.location.href = `/my/comments/` + commentId
    }else if (checkBoxes.length > 1){
        alert("댓글은 1개씩 수정 할 수 있습니다.")
    }else{
        alert("수정할 댓글을 선택해주세요.")
    }

})