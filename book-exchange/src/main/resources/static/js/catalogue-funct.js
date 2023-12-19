catalogueSearch();

function catalogueSearch() {
    let BASE_URL = "http://localhost:8080/api/catalogue/";

    let prevSearchResults =
        () => document.querySelectorAll('main.cat > div.cat-row.search-result');
    let catTable = document.querySelector('main.cat');

    const formInputValues = {
        title: () => document.getElementById('title').value,
        author: () => document.getElementById('author').value,
        genre: () => document.getElementById('genre').value,
        isCopyAvailable: () => document.getElementById('isAvailable').checked
    }

    const searchBtn = document.querySelector('.cat-row.search-form .cat-col.btn .cell.cell-btn button');
    searchBtn.addEventListener('click', searchClickHandler);

    async function searchClickHandler(event) {
        event.preventDefault();
        // TODO: delete demo rows
        const searchCriteria = getInputsData();
        prevSearchResults().forEach(row => catTable.removeChild(row));

        const http = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body:JSON.stringify(searchCriteria)
        }

        const res = await fetch(BASE_URL + 'search', http);
        const result = await res.json();

        Object.values(result).forEach(r => generateResultRow(r))
    }

    function generateResultRow(result) {
        let row = document.createElement('div');
        row.classList.add('cat-row', 'search-result');

        let titleCol = document.createElement('div');
        titleCol.classList.add('cat-col', 'cat-title');
        row.append(titleCol);
        let titleCell = document.createElement('div');
        titleCell.classList.add('cell');
        titleCell.textContent = result.title;
        titleCol.append(titleCell);

        let authorsCol = document.createElement('div');
        authorsCol.classList.add('cat-col', 'cat-author');
        row.append(authorsCol);
        let authorCell = document.createElement('div');
        authorCell.classList.add('cell');
        authorCell.textContent = result.authorsFullNames.join(', ');
        authorsCol.append(authorCell);

        let genresCol = document.createElement('div');
        genresCol.classList.add('cat-col', 'cat-genre');
        row.append(genresCol);
        let genresCell = document.createElement('div');
        genresCell.classList.add('cell');
        genresCell.textContent = result.genres.join(', ');
        genresCol.append(genresCell);

        let avCol = document.createElement('div');
        avCol.classList.add('cat-col', 'cat-available');
        row.append(avCol);
        let avCell = document.createElement('div');
        avCell.classList.add('cell')
        avCell.textContent = result.availableCopiesCount + " / " + result.totalCopiesCount;
        avCol.append(avCell);

        let btnCol = document.createElement('div');
        btnCol.classList.add('cat-col', 'btn');
        row.append(btnCol);
        let btnCell = document.createElement('div');
        btnCell.classList.add('cell');
        btnCol.append(btnCell);
        let aLink = document.createElement('a');
        let textNodeLink = document.createTextNode('Book Details');
        aLink.append(textNodeLink);
        aLink.href = BASE_URL + "books/" + result.bookId;
        btnCell.append(aLink);

        catTable.appendChild(row);
    }

    function getInputsData() {
        let searchCriteria = {};

        for (const input in formInputValues) {
            const value = formInputValues[input]();

            if (!value) {
                searchCriteria[input] = "";
            }
            searchCriteria[input] = value;
        }

        return searchCriteria;
    }

}

