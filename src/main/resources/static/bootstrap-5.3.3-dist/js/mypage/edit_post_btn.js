// 내가 쓴 게시물 페이지에서 수정 버튼을 눌렸을 경우
document.querySelector("#post-edit-btn").addEventListener("click",function () {

    // 체크된 게시물 확인
    const checkedBoxes = document.querySelectorAll(`input[name="check-num"]:checked`)


    // 체크된 박스 개수 확인
    if(checkedBoxes.length === 1){
        const boardId = checkedBoxes[0].value

        window.location.href = `/my/posts/` + boardId;
    }else if(checkedBoxes.length > 1){
        alert("게시물은 1개씩 수정할 수 있습니다")
    }else{
        alert("수정할 게시물을 선택해주세요")
    }

})