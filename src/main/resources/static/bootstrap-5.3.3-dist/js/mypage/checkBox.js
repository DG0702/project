// 전체 선택 체크 박스의 변화에 따른 체크 박스 상태
document.querySelector("#flexCheckDefault").addEventListener("change",function (){

    // 전체 선택 체크박스 상태 -> this.checked -> true,false 반환
    const isChecked = this.checked;
    
    // 페이지 내 모든 체크박스
    const checkboxes = document.querySelectorAll('input[name="check-num"]')

    // isChecked 값에 따라 체크박스 상태 변경
    checkboxes.forEach(function (checkbox){
        checkbox.checked = isChecked
    })
    
})