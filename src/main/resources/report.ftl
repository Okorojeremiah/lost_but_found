<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Living Faith Lost But Found Report</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .header {
            width: 100%;
            display: table;
            margin: 0 auto;
            padding-bottom: 10px;
        }
        .header .image-container {
            display: table-cell;
            vertical-align: middle;
            width: 200px;
        }
        .header .text-container {
            display: table-cell;
            vertical-align: middle;
            text-align: left;
            padding-left: 20px;
            font-size: 12px;
            font-style: italic;
            color: black;
        }
        .header img {
            width: 200px;
            height: 100px;
        }
        .header h4 {
            margin: 5px 0;
            font-weight: normal;
            text-transform: uppercase;
        }
        hr {
            border: 0;
            height: 1px;
            background-color: #ccc;
            margin-top: 10px;
        }
        .red-text {
            color: red;
        }
        .claims-table thead th {
            padding-bottom: 15px;
        }
        .claims-table th, .claims-table td {
            padding: 15px;
            text-align: left;
        }
    </style>
</head>
<body>
<div class="header">
    <div class="image-container">
        <img src="data:image/png;base64,${base64ImageData}" alt="winners-logo"/>
    </div>
    <div class="text-container">
        <h4>Living Faith Church Worldwide, Inc.</h4>
        <h4>Canaanland, Km. 10, Idiroko Road, Ota Ogun State, Nigeria</h4>
        <h4>Lost But Found Report</h4>
        <h4>Date: ${currentDate?string}</h4>
    </div>
</div>
<hr/>
<div>
    <p class="red-text"><strong>Report Overview: </strong></p>

    <p><strong>Date Range:</strong> ${startDate?date("yyyy-MM-dd")} to ${endDate?date("yyyy-MM-dd")}</p>
    <p><strong>Total Items Registered:</strong> ${totalItems}</p>
    <p><strong>Items Claimed:</strong> ${itemsClaimed}</p>
    <p><strong>Items Unclaimed:</strong> ${itemsUnclaimed}</p>

</div>
<hr/>
<div>
    <p class="red-text"><strong>Claims by Date:</strong></p>
    <table class="claims-table">
        <thead>
        <tr>
            <th>Date</th>
            <th>Number of Claims</th>
        </tr>
        </thead>
        <tbody>
        <#list claimsByDate as claimDate, claimCount>
            <tr>
                <td>${claimDate?string}</td>
                <td>${claimCount}</td>
            </tr>
        </#list>
        </tbody>
    </table>

    <p class="red-text"><strong>Claims by Week/Month:</strong></p>
    <table class="claims-table">
        <thead>
        <tr>
            <th>Period</th>
            <th>Number of Claims</th>
        </tr>
        </thead>
        <tbody>
        <#list claimsByMonth as period, claimCount>
            <tr>
                <td>${period}</td>
                <td>${claimCount}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <p class="red-text"><strong>Unclaimed Items Summary: </strong></p>
    <p>${unclaimedItemsSummary}</p>
</div>
</body>
</html>
