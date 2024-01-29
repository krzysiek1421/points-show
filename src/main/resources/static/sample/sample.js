window.onload = function() {
    var table = document.getElementById('table');

    var headerRow = document.createElement('tr');
    var emptyHeaderCell = document.createElement('th');
    headerRow.appendChild(emptyHeaderCell);
    var imgElement = document.createElement('img');
    imgElement.setAttribute('src', '/sample/sample.png');
    imgElement.style.maxWidth = '100px'; // Set a specific max width
    imgElement.style.maxHeight = '100px'; // Set a specific max height
    emptyHeaderCell.appendChild(imgElement);

    headerRow.appendChild(emptyHeaderCell);

    for (var i = 0; i < numTeams; i++) {
        var headerCell = document.createElement('th');
        headerCell.textContent = 'Team ' + (i + 1);
        headerCell.className = 'team-' + (i + 1);
        headerRow.appendChild(headerCell);
    }

    table.appendChild(headerRow);
    for (var j = 0; j < numCategories; j++) {
        var row = document.createElement('tr');
        var categoryCell = document.createElement('td');
        categoryCell.textContent = categories[j];
        categoryCell.className = 'category';
        row.appendChild(categoryCell);

        for (var i = 0; i < numTeams; i++) {
            var cell = document.createElement('td');
            cell.textContent = '0';
            cell.className = 'points team-' + (i + 1);
            row.appendChild(cell);
        }
        table.appendChild(row);
    }

    var totalRow = document.createElement('tr');
    var totalCell = document.createElement('td');
    totalCell.textContent = 'Total';
    totalRow.appendChild(totalCell);
    for (var i = 0; i < numTeams; i++) {
        var cell = document.createElement('td');
        cell.textContent = '0';
        totalRow.appendChild(cell);
    }
    table.appendChild(totalRow);

    function updateTableWithNewData(data) {
        if (!data || !data.categories || !data.points) {
            console.error('Invalid data received');
            return;
        }

        var table = document.getElementById('table');

        for (var catIndex = 0; catIndex < data.categories.length; catIndex++) {
            var row = table.rows[catIndex + 1];
            if (!row) continue;

            for (var teamIndex = 0; teamIndex < data.numTeams; teamIndex++) {
                var cell = row.cells[teamIndex + 1];
                if (!cell) continue;
                cell.classList.add('points-pulse-animation');
                cell.textContent = data.points[catIndex][teamIndex];

            }
        }

        var totalRow = table.rows[data.categories.length + 1];
        if (totalRow) {
            var teamTotals = calculateTeamTotals(data);
            for (var teamIndex = 0; teamIndex < data.numTeams; teamIndex++) {
                var totalCell = totalRow.cells[teamIndex + 1];
                if (totalCell) {
                    totalCell.textContent = teamTotals[teamIndex];
                }
            }
        }
    }

    function resetPoints() {
        var table = document.getElementById('table');
        for (var i = 1; i < table.rows.length - 1; i++) {
            var row = table.rows[i];
            for (var j = 1; j < row.cells.length; j++) {
                var cell = row.cells[j];
                cell.textContent = '0';
                cell.classList.remove('points-pulse-animation');
            }
        }

        // var totalRow = table.rows[table.rows.length - 1];
        // for (var i = 1; i < totalRow.cells.length; i++) {
        //     totalRow.cells[i].textContent = '0';
        // }
    }

    function calculateTeamTotals(data) {
        var teamTotals = new Array(data.numTeams).fill(0);
        for (var catIndex = 0; catIndex < data.points.length; catIndex++) {
            for (var teamIndex = 0; teamIndex < data.numTeams; teamIndex++) {
                teamTotals[teamIndex] += data.points[catIndex][teamIndex];
            }
        }
        return teamTotals;
    }

    var eventSource = new EventSource('/table-updates');
    eventSource.onmessage = function(event) {
        var data = JSON.parse(event.data);
        updateTableWithNewData(data);
    };

    eventSource.onerror = function(err) {
        console.error('EventSource failed:', err);
        eventSource.close();
    };

    window.onbeforeunload = function() {
        eventSource.close();
    };
}