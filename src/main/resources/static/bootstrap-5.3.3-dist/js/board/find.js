// 댓글 검색어 폼 제출 시
document.querySelector("#find").addEventListener("submit", function (e) {
    // 폼 제출 방지 
    e.preventDefault()

    // 입력 갑 받기
    let keyword = document.querySelector("#find-keyword").value

    // 검색어가 없을 경우
    if(keyword === ""){
        let modal = document.querySelector("#find-modal-body")
        modal.innerHTML = "<p style='text-align: center; font-weight : 700; font-size: 20px;'> 검색어를 입력해주세요 </p>"

        new bootstrap.Modal(document.querySelector("#CommentModal")).show()
        return
    }


    // 기존 게시물 숨기고, 가져오기
    document.querySelector("#comment-table").style.display = "none"
    document.querySelector("#comment-page").style.display = "none"
    document.querySelector("#comment-search-result").style.display = "block"

    try{
        fetch("/comment/search?keyword=" + encodeURIComponent(keyword))
            .then(response => {
                if(!response.ok){
                    throw new Error(response.statusText)
                }
                return response.json()
            })
            .then(data =>{
                renderSearchResults(data)
                renderPage(data)
            })
            .catch(error =>{
                alert("검색 오류 발생" + error.message)
            })
    }
    catch (error) {
        alert("예상치 못한 오류 발생" + error.message)
    }
})


// 검색 결과
function renderSearchResults (data){
    let resultContainer = document.querySelector("#comment-search-result")
    resultContainer.innerHTML = "";


    if(data.comments.length === 0){
        resultContainer.innerHTML = "<p style='text-align: center; font-weight : 700; font-size: 20px;'>등록된 게시물이 없습니다.</p>"
    }else{

        let table = document.createElement("table")

        table.className = "table table-light table-striped table-hover"

        let thead =
            `<thead>
                <tr>
                    <th scope="col">댓글 번호</th>
                    <th scope="col">게시물 번호</th>
                    <th scope="col">작성자</th>
                    <th scope="col">제목</th>
                    <th scope="col">댓글 내용 </th>
                    <th scope="col">작성일</th>
                    <th scope="col">삭제</th>
                </tr>
            </thead>`

        table.innerHTML = thead;

        let tbody = document.createElement("tbody")

        data.comments.forEach(item =>{
            let tr = document.createElement("tr")

            tr.innerHTML =
                `<td>${item.comment.boardId}</td>
                    <td>${item.comment.commentId}</td>
                    <td>${item.comment.userNickname}</td>
                    <td>
                        <a href="/posts/${item.comment.boardId}" class="text-decoration-none text-black">${item.title}</a>
                    </td>
                    <td>${item.comment.comment}</td>
                    <td>${item.comment.formattedCreateTime}</td>
                    <td>
                        <a class="text-decoration-none text-white">
                            <button type="submit" class="btn btn-danger" id="post-delete-btn">삭제</button>
                        </a>
                    </td>`
            tbody.appendChild(tr)
        })

        table.appendChild(tbody)
        resultContainer.appendChild(table)
    }
}


// 페이지
function renderPage (data){
    let resultContainer = document.querySelector("#comment-search-result")

    let nav = document.createElement("nav")

    nav.setAttribute("aria-label","Page navigation example")

    let ul = document.createElement("ul")

    ul.className = "pagination justify-content-center"

    if(data.hasPrevious){
        let li = document.createElement("li")

        li.className = "page-item"
        li.innerHTML = `<a class="allow page-link text-black" href="/comments/search?keyword=${encodeURIComponent(data.keyword)}&page=${data.previousPage}" aria-label="Previous">
                        <span aria-hidden="true">&lt;이전</span>
                        </a>`
        ul.appendChild(li)
    }

    data.pages.forEach(page => {
        let li = document.createElement("li")
        li.className = "page-item"
        li.innerHTML = `<a class="page-link text-black link-primary" href="/comments/search?keyword=${encodeURIComponent(data.keyword)}&page=${page}">${page}</a>`
        ul.appendChild(li)
    })

    if(data.hasNext){
        let li = document.createElement("li")
        li.className = "page-item"
        li.innerHTML = `<a class="allow page-link text-black" href="/comment/search?keyword=${encodeURIComponent(data.keyword)}&?page=${data.nextPage}" aria-label="Next">
                        <span aria-hidden="true">다음&gt;</span>
                        </a>`
        ul.appendChild(li)
    }

    nav.appendChild(ul)
    resultContainer.appendChild(nav)

}