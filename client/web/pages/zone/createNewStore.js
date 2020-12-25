var checkIfValidPrice = function(event) {
    return function (event) {
        if((event.key < '0' || event.key > '9') && event.key !== '.'){
            event.preventDefault();
        }
        else if(event.key === '.'){
            if(event.target.value.includes('.')){
                event.preventDefault();
            }
        }
    }
}

function validateNewStoreDetails() {
    if($('#storeId')[0].value === ""){
        alert("empty store id");
    }
    else if($('#storeName')[0].value.trim() === ""){
        alert("empty store name");
    }
    else if($('#xLocationNewStore')[0].value === ""){
        alert("empty x location")
    }
    else if($('#yLocationNewStore')[0].value === ""){
        alert("empty y location")
    }
    else if($('#PPK')[0].value === ""){
        alert("empty PPK")
    }
    else{
        ajaxIsStoreIdAvailable($('#storeId')[0].value, function () {
            ajaxIsValidLocation($('#xLocationNewStore')[0].value, $('#yLocationNewStore')[0].value, selectNewStoreItems);
        });
    }
}

function fillItemSelectTable(items){
    var tableBody = $('#newStoreItemsTableBody')[0];
    $.each(items || [] , function (index, item) {
        var row = document.createElement("tr");

        var idCell = document.createElement("td");
        var idCellText = document.createTextNode(item.itemId);
        idCell.appendChild(idCellText);
        row.appendChild(idCell);

        var nameCell = document.createElement("td");
        var nameCellText = document.createTextNode(item.name);
        nameCell.appendChild(nameCellText);
        row.appendChild(nameCell);

        var purchaseTypeCell = document.createElement("td");
        var purchaseTypeCellText = document.createTextNode(item.purchaseType);
        purchaseTypeCell.appendChild(purchaseTypeCellText);
        row.appendChild(purchaseTypeCell);

        var priceCell = document.createElement("td");

        var priceCellText = document.createElement("input");
        priceCellText.setAttribute("type","text");
        priceCellText.addEventListener("keypress", checkIfValidPrice(event));

        priceCell.appendChild(priceCellText);
        row.appendChild(priceCell);

        tableBody.appendChild(row);
    })
}

function ajaxCreateNewStore(items) {
    var urlParams = new URLSearchParams(window.location.search);
    var storeId = $('#storeId')[0].value;
    var storeName = $('#storeName')[0].value.trim();
    var xLocation = $('#xLocationNewStore')[0].value;
    var yLocation = $('#yLocationNewStore')[0].value;
    var PPK = $('#PPK')[0].value;
    $.ajax({
        url:"../../createNewStore",
        method: "POST",
        data:{
            "zone":urlParams.get("zone"),
            "items": JSON.stringify(items),
            "storeId": storeId,
            "storeName": storeName,
            "xLocation": xLocation,
            "yLocation": yLocation,
            "PPK":PPK
        },
        success: function () {
            alert("added");
            $('#addNewStoreModal').modal('hide');
        }
    })
}

function validateItemsSelected() {
    var rows = $('#newStoreItemsTableBody')[0].childNodes;
    var items = [];
    $.each(rows || [], function (index, row) {
        if(row.childNodes[3].childNodes[0].value !== "" && row.childNodes[3].childNodes[0].value !== '0'){
            items[items.length] = {
                "itemId": row.childNodes[0].textContent,
                "price": row.childNodes[3].childNodes[0].value
            }
        }
    })

    if(items.length === 0){
        alert("no items selected");
    }
    else{
        ajaxCreateNewStore(items);
    }
}

function selectNewStoreItems(isValidLocation) {
    if(isValidLocation === "true") {
        ajaxZoneItems(fillItemSelectTable);
        $("#selectItemsButton")[0].onclick = validateItemsSelected;
        $("#selectItemsButton")[0].innerText = "Create store";

        $('#addNewStoreHeader').hide();
        $('#selectItemsHeader').show();
        $('#newStoreDetails').hide();
        $('#newStoreItemsTable').show();
    }
    else{
        alert("location is taken")
    }
}

function ajaxIsStoreIdAvailable(storeId, callback){
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../isStoreIdAvailable",
        data: {"storeId":storeId, "zone":urlParams.get("zone")},
        success: function (isValid) {
            if(isValid === "true"){
                callback();
            }
            else{
                alert("ERROR. store id already taken.")
            }
        }
    })
}

$(function () {
    $('#xLocationNewStore')[0].onkeypress = isInteger(event);
    $('#yLocationNewStore')[0].onkeypress = isInteger(event);
    $('#storeId')[0].onkeypress = isInteger(event);
    $('#PPK')[0].onkeypress = checkIfValidPrice(event);

    $('#addNewStoreModal').on('hidden.bs.modal', function (e) {
        $("#selectItemsButton")[0].onclick = validateNewStoreDetails;
        $("#selectItemsButton")[0].innerText = "Next";

        $('#newStoreItemsTableBody').empty();
        $('#addNewStoreHeader').show();
        $('#selectItemsHeader').hide();
        $('#newStoreDetails').show();
        $('#newStoreItemsTable').hide();

        $('#xLocationNewStore')[0].value = "";
        $('#storeName')[0].value = "";
        $('#yLocationNewStore')[0].value = "";
        $('#storeId')[0].value = "";
        $('#PPK')[0].value = "";

    })
})