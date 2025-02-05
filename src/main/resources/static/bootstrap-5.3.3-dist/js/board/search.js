 // 폼 제출시
    document.querySelector("#search").addEventListener("submit",function (e) {

        let resultContainer = document.querySelector("#search-result");

        // 폼 제출 방지
        e.preventDefault()


        // 입력 값 받기
        let keyword = document.querySelector("#search-keyword").value

        // 검색어가 없을 경우
        if (keyword === "") {
            // 경고 메시지
            let modal = document.querySelector("#modal-body");
            modal.innerHTML = "<p style='text-align: center; font-weight : 700; font-size: 20px;'> 검색어를 입력해주세요 </p>"

            new bootstrap.Modal(document.querySelector("#idModal")).show()
            return;
        }



        // 기존 게시물 숨기기
        document.querySelector("#board-table").style.display = "none";
        // 기존 페이지 숨기기
        document.querySelector("#page").style.display = "none";

        // 검색 결과 출력
        resultContainer.style.display = "block";

        // 비동기 요청
        try{
            fetch("/board/search?keyword=" + encodeURIComponent(keyword))
                .then(response => {
                    if(!response.ok){
                        throw new Error(response.statusText)
                    }
                    return response.json()
                })
                .then(data => {
                    // 검색 게시물 나열
                    console.log(data)
                    console.log(data.content)
                    renderSearchResults(data)
                    // 검색 게시물 페이징
                    renderPage(data)
                })
                .catch(error =>{
                    alert("검색 오류 발생 " + error.message)
                })
        }
        catch (error) {
            alert("예상치 못한 오류 발생 " + error.message)
        }
    })



// 검색 결과를 표시하는 함수
function renderSearchResults(data) {
    try {
        
        let resultContainer = document.querySelector("#search-result")

        resultContainer.innerHTML = "";

        // 검색 결과 없을 경우
        if (data.content.length === 0) {
            resultContainer.innerHTML = "<p style='text-align: center; font-weight : 700; font-size: 20px;'>등록된 게시물이 없습니다.</p>"
        } else {
            // 검색 결과 게시물을 보여줄 테이블을 생성
            let table = document.createElement("table")

            // table 클래스 추가
            table.className = "table table-light table-striped table-hover"
            // thead 생성
            let thead =
                `<thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">제목</th>
                        <th scope="col">작성자</th>
                        <th scope="col">작성일</th>
                        <th scope="col">조회</th>
                        <th scope="col">좋아요</th>
                        <th scope="col">댓글</th>
                    </tr>
                </thead>`

            // table 내용 추가, innerHtml = 기존값을 덮어씌움
            table.innerHTML = thead;

            // tbody 생성
            let tbody = document.createElement("tbody")

            // 검색 결과 게시물 존재시
            // foreach 반복문 활용하여 board 객체로 설정 -> board.속성
            data.content.forEach(item => {
                let tr = document.createElement("tr")
                tr.innerHTML =
                    `<th scope="row">${item.board.boardId}</th>
                    <td>
                        <a href="#" class="text-decoration-none text-black">${item.board.title}</a>
                    </td>
                    <td>${item.board.userNickname}</td>
                    <td>${item.board.formattedCreateTime}</td>
                    <td>${item.board.viewCount}</td>
                    <td>${item.likesCount}</td>
                    <td>${item.commentCount}</td>`

                // tbody에 tr를 자식요소로 추가
                tbody.appendChild(tr)
            })

            // table에 tbody를 자식요소로 추가
            table.appendChild(tbody)
            // resultContainer에 table를 자식요소로 추가
            resultContainer.appendChild(table)

        }
    } catch (error) {
        console.error("renderSearchResults 에러 발생 ", error.message)
    }
}

// 페이지
function renderPage (data){

    let resultContainer = document.querySelector("#search-result")
    
    // nav 생성
    let nav = document.createElement("nav")
    
    // nav 속성, 속성값 추가
    nav.setAttribute("aria-label","Page navigation example");

    // ul 추가
    let ul = document.createElement("ul")
    
    // ul class 추가
    ul.className = "pagination justify-content-center"
    
    // 이전 버튼 활성화 시 
    if(data.hasPrevious){
        // li 생성
        let li = document.createElement("li")
        
        // li 클래스 추가
        li.className = "page-item"
        
        // li 내용 추가
        li.innerHTML = ` <a class="allow page-link text-black" href="/posts/search?keyword=${encodeURIComponent(data.keyword)}&page=${data.previousPage}" aria-label="Previous">
                        <span aria-hidden="true">&lt;이전</span>
                        </a>`
        
        // ul 자식요소로 추가
        ul.appendChild(li)
    }
    
    // 검색 결과 페이지 존재 시
    data.pages.forEach(page => {
        // li 생성
        let li = document.createElement("li")
        
        // li 클래스 추가
        li.className = "page-item"
        
        // li 내용 추가
        li.innerHTML = `<a class="page-link text-black link-primary" href="/posts/search?keyword=${encodeURIComponent(data.keyword)}&page=${page}">${page}</a>`
        
        // ul 자식요소로 추가
        ul.appendChild(li)
    })

    // 다음 버튼 활성화 시
    if(data.hasNext){
        // li 생성
        let li = document.createElement("li")
        
        // li 클래스 추가
        li.className = "page-item"
        
        // li 내용 추가
        li.innerHTML = `<a class="allow page-link text-black" href="/posts/search?keyword${encodeURIComponent(data.keyword)}&page=${data.nextPage}" aria-label="Next">
                        <span aria-hidden="true">다음&gt;</span>
                         </a>`
        
        // ul 자식요소로 추가
        ul.appendChild(li)
    }
    
    // nav 자식요소로 추가
    nav.appendChild(ul)
    
    // id값 search-result  자식요소로 추가
    resultContainer.appendChild(nav);

}
