function ajaxUpdateUsers() {
    $.ajax({
        url: "../../updateUsers",
        success: function (users) {
            $("#usersTableRows").empty();
            var tblBody = document.getElementById("usersTableRows");
            $.each(users || [], function(index, user) {

                var row = document.createElement("tr");
                var usernameCell = document.createElement("td");
                var usernameCellText = document.createTextNode(user.name);
                usernameCell.appendChild(usernameCellText);
                row.appendChild(usernameCell);
                var userTypeCell = document.createElement("td");
                var userTypeCellText
                if(user.stores !== undefined){
                    userTypeCellText = document.createTextNode("Store owner");
                }
                 else{
                    userTypeCellText = document.createTextNode("Customer");
                }
                userTypeCell.appendChild(userTypeCellText);
                row.appendChild(userTypeCell);
                tblBody.appendChild(row)

            });
        }
    })
}

function ajaxAddMoneyToUser() {
    var amount = $('#amountOfMoney')[0].value;
    var date = $('#transactionDate')[0].value;
    $.ajax({
        method: 'POST',
        url: '../../addMoney',
        data: {'amount': amount, "date": date},
        success: function (message) {
            alert(message);
            $('#amountOfMoney')[0].value = "";
            $('#transactionDate')[0].value = "";
        },
        error: function (message) {
            alert(message);
        }
    })
}

$(function () {
    ajaxUserData();
    setInterval(ajaxUpdateUsers,1000);
    setInterval(ajaxUpdateZones,1000);
    setInterval(ajaxUserBalance,1000);
    $('#transactionsHistoryModal').on('hidden.bs.modal', function (e) {
        $('#transactionsHistoryBody').empty();
    })

})

var zoneClickEventHandler = function (zoneName) {
    return function () {
        window.location = '../zone/zone.html?zone=' + zoneName;
    }
}

function ajaxUpdateZones() {
    $.ajax({
        url: "../../updateZones",
        success: function (zone) {
            $("#zonesTableRows").empty();
            var tableBody = document.getElementById("zonesTableRows");

            $.each(zone || [], function(index, zone) {
                var row = document.createElement("tr");
                row.onclick = zoneClickEventHandler(zone.zoneName);

                var zoneOwnerCell = document.createElement("td");
                var zoneOwnerCellText = document.createTextNode(zone.ownerName);
                zoneOwnerCell.appendChild(zoneOwnerCellText);
                row.appendChild(zoneOwnerCell);

                var zoneNameCell = document.createElement("td");
                var zoneNameCellText = document.createTextNode(zone.zoneName);
                zoneNameCell.appendChild(zoneNameCellText);
                row.appendChild(zoneNameCell);

                var numberOfItemTypesCell = document.createElement("td");
                var numberOfItemTypesCellText = document.createTextNode(zone.numberOfItemsType);
                numberOfItemTypesCell.appendChild(numberOfItemTypesCellText);
                row.appendChild(numberOfItemTypesCell);

                var numberOfStoresCell = document.createElement("td");
                var numberOfStoresCellText = document.createTextNode(zone.numberOfStores);
                numberOfStoresCell.appendChild(numberOfStoresCellText);
                row.appendChild(numberOfStoresCell);

                var numberOfOrdersCell = document.createElement("td");
                var numberOfOrdersCellText = document.createTextNode(zone.numberOfOrders);
                numberOfOrdersCell.appendChild(numberOfOrdersCellText);
                row.appendChild(numberOfOrdersCell);

                var orderAverageCostCell = document.createElement("td");
                var orderAverageCostCellText = document.createTextNode(zone.orderAvgCost);
                orderAverageCostCell.appendChild(orderAverageCostCellText);
                row.appendChild(orderAverageCostCell);

                tableBody.appendChild(row);
            })
        }

    })
}

function ajaxUserData() {
    $.ajax({
        url: "../../getUserData",
        success: function(user) {
            if(user['stores'] === undefined){
                $('#uploadXmlMenuButton').hide();
            }
            if(user['stores'] !== undefined){
                $('#addMoneyToAccount').hide();
                startStoreOwnerNotifications();
            }
            if(sessionStorage.getItem("username") === null){
                sessionStorage.setItem("username", user.name);
            }
            $('#greeting')[0].innerHTML = "Welcome " + user.name + ",";
        },
        error:function () {
            console.log("error");
        }
    });
}

function ajaxUserBalance() {
    $.ajax({
        url: "../../getUserBalance",
        success: function(balance) {
            $('#currentBalance').empty();
            $('#currentBalance').append(document.createTextNode("Account balance: " + balance));
        }
    });
}

function ajaxTransactionsHistory() {
    $.ajax({
        url:"../../transactionsHistory",
        success: function (transactions) {
            $("#transactionsHistoryBody").empty();
            var tblBody = document.getElementById("transactionsHistoryBody");

            $.each(transactions || [] , function (index, transaction) {
                var row = document.createElement("tr");

                var typeCell = document.createElement("td");
                var typeCellText = document.createTextNode(transaction.transactionType.replace(/([A-Z])/g, ' $1').trim());
                typeCell.appendChild(typeCellText);
                row.appendChild(typeCell);

                var dateCell = document.createElement("td");
                var dateCellText = document.createTextNode(transaction.date);
                dateCell.appendChild(dateCellText);
                row.appendChild(dateCell);

                var amountCell = document.createElement("td");
                var amountCellText = document.createTextNode(transaction.amount);
                amountCell.appendChild(amountCellText);
                row.appendChild(amountCell);

                var balanceBeforeCell = document.createElement("td");
                var balanceBeforeCellText = document.createTextNode(transaction.balanceBeforeTransaction);
                balanceBeforeCell.appendChild(balanceBeforeCellText);
                row.appendChild(balanceBeforeCell);

                var balanceAfterCell = document.createElement("td");
                var balanceAfterCellText = document.createTextNode(transaction.balanceAfterTransaction);
                balanceAfterCell.appendChild(balanceAfterCellText);
                row.appendChild(balanceAfterCell);

                tblBody.appendChild(row);
            })
        }
    })
}

function uploadXml(){
    if(!$("form input[type=file]").val()) {
        alert('You must select a file!');
    }
    else {
        var form = new FormData();
        var files = $('#xmlFIle')[0].files[0];
        form.append('file', files);

        $.ajax({
            type: "POST",
            url: "uploadFile",
            data: form,
            contentType: false,
            processData: false,
            success: function (message) {
                alert(message);
            },
            error: function (message) {
                alert(message)
            }
        })
    }
}