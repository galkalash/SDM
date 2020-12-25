var storeId;
var itemsInOrder = [];
var availableDiscounts =[];
var thereAreDiscounts;

var ajaxGetDeliveryCost = function(storesDropDown) {
    return function () {
        var xLocation = $("#xLocation")[0].value;
        var yLocation = $("#yLocation")[0].value;
        var storeId = storesDropDown.selectedOptions[0].label.split(" ",1)[0];
        var urlParams = new URLSearchParams(window.location.search);
        $.ajax({
            url: "../../calculateDeliveryCost",
            data: {"X" : xLocation, "Y" : yLocation, "storeId":storeId, "zone":urlParams.get("zone")},
            success: function (deliveryCost) {
                $("#deliveryCost").text("Delivery cost: " + deliveryCost);
            },
            error: function (message) {
                alert(message);
            }
        })
    }
}

function filterItems(storeItems) {
    var tableRows = $('#itemsTable>tbody')[0].childNodes;
    $.each(tableRows || [], function (rowIndex, row) {
        $.each(JSON.parse(storeItems) || [], function (itemIndex, item) {
            if(tableRows[rowIndex].childNodes[0].childNodes[0].data == item.id){
                tableRows[rowIndex].childNodes[3].childNodes[0].data = item.price;
                tableRows[rowIndex].childNodes[4].childNodes[0].disabled = false;
            }
        })
    })
}

function ajaxStoreItems(storeId) {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getStoreItems",
        data: {"zone":urlParams.get("zone"), "storeId" : storeId},
        success: function (storeItems) {
            filterItems(storeItems);
        },
        error: function (message) {
            alert(message);
        }
    })
}

var checkIfNumber = function(event) {
    return function (event) {
        if((event.key < '0' || event.key > '9') && event.key !== '.'){
            event.preventDefault();
        }
        else if(event.key === '.'){
            if(event.target.parentElement.parentElement.childNodes[2].textContent === "Quantity"){
                event.preventDefault();
            }
            else if(event.target.value.includes('.')){
                event.preventDefault();
            }
        }
    }
}

function showAllItems(items, storeId) {
    $('#orderHeader')[0].textContent = "Select Items";
    var tableDiv = document.getElementById("orderDynamicFields");
    var nextButton = document.createElement("button");
    nextButton.textContent = "Next";
    nextButton.id = "selectDiscounts";
    nextButton.onclick = getSelectedItems();
    nextButton.classList.add("modalNextButton");
    nextButton.classList.add("btn-secondary");
    nextButton.classList.add("btn");
    var table = document.createElement("table");
    table.id = "itemsTable";
    var thead = document.createElement("thead");
    thead.classList.add("thead-dark")
    var tbody = document.createElement("tbody");
    table.appendChild(thead);
    table.appendChild(tbody);
    table.classList.add("table");
    table.classList.add("table-hover");
    table.classList.add("table-striped");
    tableDiv.appendChild(table);
    tableDiv.appendChild(nextButton);
    var headerRow = document.createElement("tr");
    thead.appendChild(headerRow);
    $('#itemsTable>thead>tr').append("<th>ID</th>");
    $('#itemsTable>thead>tr').append("<th>Name</th>");
    $('#itemsTable>thead>tr').append("<th>Purchase type</th>");
    if($("#staticOrder")[0].checked === true) {
        $('#itemsTable>thead>tr').append("<th>Price</th>");
    }
    $('#itemsTable>thead>tr').append("<th>Quantity</th>");

    $.each(items || [], function (index, item) {
        var row = document.createElement("tr");

        var itemIdCell = document.createElement("td");
        var itemIdCellText = document.createTextNode(item.itemId);
        itemIdCell.appendChild(itemIdCellText);
        row.appendChild(itemIdCell);

        var itemNameCell = document.createElement("td");
        var itemNameCellText = document.createTextNode(item.name);
        itemNameCell.appendChild(itemNameCellText);
        row.appendChild(itemNameCell);

        var purchaseTypeCell = document.createElement("td");
        var purchaseTypeCellText = document.createTextNode(item.purchaseType);
        purchaseTypeCell.appendChild(purchaseTypeCellText);
        row.appendChild(purchaseTypeCell);

        if($("#staticOrder")[0].checked === true) {
            var priceCell = document.createElement("td");
            var priceCellText = document.createTextNode("not being sold");
            priceCell.appendChild(priceCellText);
            row.appendChild(priceCell);
        }

        var quantityCell = document.createElement("td");
        var quantityCellText = document.createElement("input");
        quantityCellText.setAttribute("type","text");
        quantityCellText.addEventListener("keypress", checkIfNumber(event));
        if($("#staticOrder")[0].checked === true) {
            quantityCellText.disabled = true;
        }
        quantityCell.appendChild(quantityCellText);
        row.appendChild(quantityCell);

        tbody.appendChild(row);
    });
    if($("#staticOrder")[0].checked === true) {
        ajaxStoreItems(storeId);
    }
}

var selectItems = function () {
    return function (){
        if($("#staticOrder")[0].checked === true) {
            if ($('#storesDropDown')[0].value === "") {
                alert("ERROR. no store selected");
                return;
            }
            storeId = $('#storesDropDown')[0].value.split(" ", 1)[0];
        }
        $('#orderDynamicFields').empty();
        var urlParams = new URLSearchParams(window.location.search);
        $.ajax({
            url: "../../getZoneItems",
            data: {"zone": urlParams.get('zone')},
            success: function (items) {
                showAllItems(items, storeId);
            },
            error: function (message) {
                alert(message);
            }
        })
    }
}

var enableDropdown = function() {
    return function() {
        if(event.target.checked === true) {
            event.target.parentElement.childNodes[4].disabled = false;
        }
        else{
            event.target.parentElement.childNodes[4].disabled = true;
        }

        reCalculateAvailableDiscounts().call();
    }
}

function updateAvailableDiscounts(selectedDiscountIndex) {
    var quantitiesUsedForDiscounts = 0;
    var quantityBought = 0
    var map = [];
    var discountsDiv = $("#orderDynamicFields")[0];
    var discountStoreId;
    if(discountsDiv.childNodes[selectedDiscountIndex].childNodes[6] === undefined){
        discountStoreId = discountsDiv.childNodes[selectedDiscountIndex].childNodes[4].textContent;
    }
    else{
        discountStoreId = discountsDiv.childNodes[selectedDiscountIndex].childNodes[5].textContent;
    }

    for(var i = 0;i<itemsInOrder[discountStoreId].length ; i++){
        if(itemsInOrder[discountStoreId][i].itemId == availableDiscounts[selectedDiscountIndex].condition.itemId){
            quantityBought = itemsInOrder[discountStoreId][i].quantity;
            break;
        }
    }

    for(var i = 0; i<discountsDiv.childNodes.length - 1 ; i++){
        if(availableDiscounts[i].condition.itemName === availableDiscounts[selectedDiscountIndex].condition.itemName &&
            discountsDiv.childNodes[i].childNodes[0].checked){
                quantitiesUsedForDiscounts += availableDiscounts[i].condition.quantity;
        }
    }

    var quantityLeftForDiscounts = quantityBought - quantitiesUsedForDiscounts;

    for(var i = 0; i<discountsDiv.childNodes.length - 1 ; i++){
        if(discountsDiv.childNodes[i].childNodes[1].childNodes[0].textContent !== discountsDiv.childNodes[selectedDiscountIndex].childNodes[1].childNodes[0].textContent &&
            availableDiscounts[i].condition.itemName === availableDiscounts[selectedDiscountIndex].condition.itemName &&
            !discountsDiv.childNodes[i].childNodes[0].checked){
            if(map[discountsDiv.childNodes[i].childNodes[1].childNodes[0].textContent] === undefined){
                map[discountsDiv.childNodes[i].childNodes[1].childNodes[0].textContent] = 0;
            }
            if(map[discountsDiv.childNodes[i].childNodes[1].childNodes[0].textContent] + availableDiscounts[i].condition.quantity > quantityLeftForDiscounts){
                discountsDiv.childNodes[i].hidden = true;
            }
            else if(map[discountsDiv.childNodes[i].childNodes[1].childNodes[0].textContent] + availableDiscounts[i].condition.quantity <= quantityLeftForDiscounts){
                map[discountsDiv.childNodes[i].childNodes[1].childNodes[0].textContent] += availableDiscounts[i].condition.quantity;
                discountsDiv.childNodes[i].hidden = false;
            }
        }
    }
}

var reCalculateAvailableDiscounts = function() {
    return function () {
        var selectedDiscountIndex = -1;
        for(var i = 0;i<event.target.parentElement.parentElement.childNodes.length; i++){
            if(event.target === event.target.parentElement.parentElement.childNodes[i].childNodes[0]){
                selectedDiscountIndex = i;
            }
        }
        updateAvailableDiscounts(selectedDiscountIndex);
    };
}

function showStoreDetails(orderSummary) {
    var urlParams = new URLSearchParams(window.location.search);
    var currentStoreId = storeId;
    $.ajax({
        url: "../../storeDetails",
        data:{"storeId":storeId, "zone":urlParams.get("zone")},
        success: function (store) {
            if($('#staticOrder')[0].checked) {
                $('#'+'storeDetails'+currentStoreId)[0].innerHTML = "Store ID: " + currentStoreId + "<br>" + "Store name: " + store.name + "<br>" +
                    "Delivery price per kilometer: " + store.deliveryPpk + "<br>" + "Distance from store: " + orderSummary.orderDetails.distance +
                    "<br>" + "Delivery cost: " + orderSummary.orderDetails.deliveryCost;
            }
            else{
                $('#'+'storeDetails'+currentStoreId)[0].innerHTML = "Store ID: " + currentStoreId + "<br>" + "Store name: " + store.name + "<br>" +
                    "Delivery price per kilometer: " + store.deliveryPpk + "<br>" + "Distance from store: " + orderSummary.distance +
                    "<br>" + "Delivery cost: " + orderSummary.deliveryCost;
            }
        },
        error: function (message) {
            alert(message);
        }
    })
}

function showOrderSummary(orderSummary) {
    var singleStoreOrderDiv = document.createElement("div");
    var storeDetails = document.createElement("div");
    storeDetails.id = "storeDetails" + storeId;
    $("#orderDynamicFields")[0].appendChild(singleStoreOrderDiv);
    var table = document.createElement("table");
    singleStoreOrderDiv.appendChild(storeDetails);
    singleStoreOrderDiv.appendChild(table);
    table.id = "orderSummaryItemsTable"+storeId;
    table.classList.add("table");
    table.classList.add("table-hover");
    table.classList.add("table-striped");
    var tbody = document.createElement("tbody");
    var thead = document.createElement("thead");
    thead.classList.add("thead-dark");
    table.appendChild(thead);
    var headerRow = document.createElement("tr");
    thead.appendChild(headerRow);
    var tableId = 'orderSummaryItemsTable' + storeId;
    $('#'+tableId+'>thead>tr').append("<th>ID</th>");
    $('#'+tableId+'>thead>tr').append("<th>Name</th>");
    $('#'+tableId+'>thead>tr').append("<th>Purchase type</th>");
    $('#'+tableId+'>thead>tr').append("<th>Quantity</th>");
    $('#'+tableId+'>thead>tr').append("<th>Price</th>");
    $('#'+tableId+'>thead>tr').append("<th>Total price</th>");
    $('#'+tableId+'>thead>tr').append("<th>Purchased from discount</th>");

    table.appendChild(tbody);
    var items;
    if($("#staticOrder")[0].checked) {
        items = orderSummary.orderDetails.items;
    }
    else{
        items = orderSummary.items;
    }
    for(var i = 0;i<items.length ; i++){
        var row = document.createElement("tr");
        tbody.appendChild(row);

        var itemIdCell = document.createElement("td");
        var itemIdCellText = document.createTextNode(items[i].orderedItem.itemId);
        itemIdCell.appendChild(itemIdCellText);
        row.appendChild(itemIdCell);

        var itemNameCell = document.createElement("td");
        var itemNameCellText = document.createTextNode(items[i].orderedItem.name);
        itemNameCell.appendChild(itemNameCellText);
        row.appendChild(itemNameCell);

        var purchaseTypeCell = document.createElement("td");
        var purchaseTypeCellText = document.createTextNode(items[i].orderedItem.purchaseType);
        purchaseTypeCell.appendChild(purchaseTypeCellText);
        row.appendChild(purchaseTypeCell);

        var quantityCell = document.createElement("td");
        var quantityCellText = document.createTextNode(items[i].quantity);
        quantityCell.appendChild(quantityCellText);
        row.appendChild(quantityCell);

        var priceCell = document.createElement("td");
        var priceCellText = document.createTextNode(items[i].price);
        priceCell.appendChild(priceCellText );
        row.appendChild(priceCell);

        var totalPriceCell = document.createElement("td");
        var totalPriceCellText = document.createTextNode(items[i].totalPrice);
        totalPriceCell.appendChild(totalPriceCellText );
        row.appendChild(totalPriceCell);

        var purchasedFromDiscountCell = document.createElement("td");
        var purchasedFromDiscountCellText;
        if(items[i].isPurchaseWithDiscount){
            purchasedFromDiscountCellText = document.createTextNode('V');
        }
        else{
            purchasedFromDiscountCellText = document.createTextNode('X');
        }

        purchasedFromDiscountCell.appendChild(purchasedFromDiscountCellText );
        row.appendChild(purchasedFromDiscountCell);
    }

    showStoreDetails(orderSummary);
}

var placeOrder = function(orderSummary) {
    return function () {
        var urlParams = new URLSearchParams(window.location.search);
        $.ajax({
            url: "../../placeStaticOrder",
            method: "POST",
            data: {"order":JSON.stringify(orderSummary),"zone": urlParams.get("zone")},
            success: function (orderId) {
                $("#orderDynamicFields").empty();
                getFeedback(storeId);
                var feedbackDiv = $('#orderDynamicFields')[0];
                var buttonsDiv = document.createElement("div");
                buttonsDiv.classList.add("modalButtonsDiv");
                var submit = document.createElement('button');
                submit.textContent = "Submit";
                submit.setAttribute("data-dismiss","modal");
                submit.classList.add("btn");
                submit.classList.add("btn-secondary");
                submit.onclick = submitFeedback(orderId)
                var cancel = document.createElement('button');
                cancel.textContent = "Cancel";
                cancel.classList.add("modalCancelButton");
                cancel.classList.add("btn");
                cancel.classList.add("btn-secondary");
                cancel.setAttribute("data-dismiss","modal");
                buttonsDiv.appendChild(submit);
                buttonsDiv.appendChild(cancel);
                feedbackDiv.appendChild(buttonsDiv);
            },
            error: function (message) {
                alert(message);
            }
        })
    };
}

var cancelOrder = function() {
    return function () {
        alert("The order has been canceled.");
    };
}

function showStaticOrderSummary(orderSummary) {
    $("#orderDynamicFields").empty();
    showOrderSummary(orderSummary);
    var totalPriceDiv = document.createElement("div");
    var footer = document.createElement("div");
    footer.id = "orderSummaryButtons";
    footer.classList.add("modalButtonsDiv");
    var confirm = document.createElement("button");
    confirm.textContent = "Confirm";
    confirm.classList.add("btn");
    confirm.classList.add("btn-secondary");
    confirm.onclick = placeOrder(orderSummary);
    var cancel = document.createElement("button");
    cancel.textContent = "Cancel";
    cancel.classList.add("modalCancelButton");
    cancel.classList.add("btn-secondary");
    cancel.classList.add("btn");
    cancel.onclick = cancelOrder();
    cancel.setAttribute("data-dismiss","modal");
    $("#orderDynamicFields")[0].appendChild(totalPriceDiv);
    $("#orderDynamicFields")[0].appendChild(footer);
    var totalPrice = document.createElement("p");
    var totalPriceNumber =  +orderSummary.orderDetails.itemsCost + +orderSummary.orderDetails.deliveryCost;
    totalPrice.innerHTML = "Items cost: " + orderSummary.orderDetails.itemsCost + "  Delivery cost: " + orderSummary.orderDetails.deliveryCost +
        "  Total price: " + totalPriceNumber;
    totalPriceDiv.appendChild(totalPrice);
    footer.appendChild(confirm);
    footer.appendChild(cancel);
}

function toObject(array) {
    var result = {};
    for(var i = 0;i<array.length ; i++){
        if(array[i] !== undefined){
            result[i] = array[i]
        }
    }

    return result;
}

function getFeedback(id) {
    $('#orderHeader')[0].textContent = "The order has been placed successfully.";
    $('#headerMessage')[0].textContent = "Please enter feedback";
    var feedbackDiv = $('#orderDynamicFields')[0];
    var singleFeedbackDiv = document.createElement('div');
    feedbackDiv.appendChild(singleFeedbackDiv);
    var header = document.createElement('p');
    var rating = document.createElement('p');

    header.innerHTML = "Please enter you feedback for store id: " + id + "<br>";
    rating.innerHTML = "Rate the store from 1 (low) to 5 (high): <br>";

    var oneStar = document.createElement('input');
    oneStar.id = 'oneStar'+id;
    oneStar.setAttribute('type','radio');
    oneStar.setAttribute('name','rating'+id);
    var oneStarLabel = document.createElement('label');
    oneStarLabel.setAttribute('for','oneStar'+id);
    oneStarLabel.textContent = '1';

    var twoStars = document.createElement('input');
    twoStars.setAttribute('type','radio');
    twoStars.setAttribute('name','rating'+id);
    twoStars.id = 'twoStars'+id;
    var twoStarsLabel = document.createElement('label');
    twoStarsLabel.setAttribute('for','twoStars'+id);
    twoStarsLabel.textContent = '2';

    var threeStars = document.createElement('input');
    threeStars.setAttribute('type','radio');
    threeStars.setAttribute('name','rating'+id);
    threeStars.id = "threeStars"+id;
    var threeStarsLabel = document.createElement('label');
    threeStarsLabel.setAttribute('for','threeStars'+id);
    threeStarsLabel.textContent = '3';

    var fourStars = document.createElement('input');
    fourStars.setAttribute('type','radio');
    fourStars.setAttribute('name','rating'+id);
    fourStars.id = 'fourStars'+id;
    var fourStarsLabel = document.createElement('label');
    fourStarsLabel.setAttribute('for','fourStars'+id);
    fourStarsLabel.textContent = '4';

    var fiveStars = document.createElement('input');
    fiveStars.setAttribute('type','radio');
    fiveStars.setAttribute('name','rating'+id);
    fiveStars.id = 'fiveStars'+id;
    var fiveStarsLabel = document.createElement('label');
    fiveStarsLabel.setAttribute('for','fiveStars'+id);
    fiveStarsLabel.textContent = '5';

    var feedbackText = document.createElement('textarea');
    feedbackText.id = 'feedbackText';

    singleFeedbackDiv.appendChild(header);
    singleFeedbackDiv.appendChild(rating);
    singleFeedbackDiv.appendChild(oneStarLabel);
    singleFeedbackDiv.appendChild(oneStar);
    singleFeedbackDiv.appendChild(twoStarsLabel);
    singleFeedbackDiv.appendChild(twoStars);
    singleFeedbackDiv.appendChild(threeStarsLabel);
    singleFeedbackDiv.appendChild(threeStars);
    singleFeedbackDiv.appendChild(fourStarsLabel);
    singleFeedbackDiv.appendChild(fourStars);
    singleFeedbackDiv.appendChild(fiveStarsLabel);
    singleFeedbackDiv.appendChild(fiveStars);
    singleFeedbackDiv.innerHTML += '<br><br>'
    singleFeedbackDiv.appendChild(feedbackText);
}

var submitFeedback = function(orderId) {
    return function () {
        var feedbacksDiv = $('#orderDynamicFields')[0].childNodes;
        var urlParams = new URLSearchParams(window.location.search);
        for(var i = 0; i< feedbacksDiv.length - 1; i++){
            var storeId = feedbacksDiv[i].childNodes[0].textContent.split(' ');
            storeId = storeId[storeId.length-1];
            var selector = 'input[name=rating' + storeId + ']:checked';
            var selectedRating = document.querySelector(selector);
            if(selectedRating !== null) {
                var rating = selectedRating.labels[0].textContent;
                var feedbackText = feedbacksDiv[i].childNodes[feedbacksDiv[i].childNodes.length - 1].value;
                var date = $("#deliveryDate")[0].value;
                $.ajax({
                    url: "../../addFeedback",
                    method: "POST",
                    data: {
                        "zone": urlParams.get("zone"),
                        "storeId": storeId,
                        "orderId": orderId,
                        "rating": rating,
                        "feedback": feedbackText,
                        "date": date
                    },
                    success: function () {
                        alert("added");
                    },
                    error: function () {

                    }
                })
            }
        }
    };
}

var placeDynamicOrder = function(orderSummary) {
    return function () {
        var urlParams = new URLSearchParams(window.location.search);
        $.ajax({
            url: "../../placeDynamicOrder",
            method: "POST",
            data: {"zone": urlParams.get("zone"), "order":JSON.stringify(orderSummary)},
            success: function (orderId) {
                $('#orderDynamicFields').empty();
                $.each(orderSummary.ordersFromStores || [], function(id,orderFromStore){
                    getFeedback(id)
                })
                var feedbackDiv = $('#orderDynamicFields')[0];
                var buttonsDiv = document.createElement("div");
                buttonsDiv.classList.add("modalButtonsDiv");
                var submit = document.createElement('button');
                submit.textContent = "Submit";
                submit.setAttribute("data-dismiss","modal");
                submit.onclick = submitFeedback(orderId)
                submit.classList.add("btn");
                submit.classList.add("btn-secondary");
                var cancel = document.createElement('button');
                cancel.textContent = "Cancel";
                cancel.setAttribute("data-dismiss","modal");
                cancel.classList.add("modalCancelButton");
                cancel.classList.add("btn");
                cancel.classList.add("btn-secondary");
                buttonsDiv.appendChild(submit);
                buttonsDiv.appendChild(cancel);
                feedbackDiv.appendChild(buttonsDiv);
            },
            error: function (message) {
                alert(message);
            }
        })
    };
}

function showDynamicOrderSummary(orderSummary) {
    var stores = orderSummary.ordersFromStores;
    $("#orderDynamicFields").empty();
    $.each(stores || [], function (storeIdIndex, orderFromStore) {
        storeId = storeIdIndex;
        showOrderSummary(orderFromStore);
    })
    var totalPriceDiv = document.createElement("div");
    var footer = document.createElement("div");
    footer.classList.add("modalButtonsDiv");
    var confirm = document.createElement("button");
    confirm.textContent = "Confirm";
    confirm.classList.add("btn");
    confirm.classList.add("btn-secondary");
    confirm.onclick = placeDynamicOrder(orderSummary);
    var cancel = document.createElement("button");
    cancel.textContent = "Cancel";
    cancel.classList.add("modalCancelButton");
    cancel.classList.add("btn");
    cancel.classList.add("btn-secondary");
    cancel.onclick = cancelOrder();
    cancel.setAttribute("data-dismiss","modal");
    $("#orderDynamicFields")[0].appendChild(totalPriceDiv);
    $("#orderDynamicFields")[0].appendChild(footer);
    var totalPrice = document.createElement("p");
    var totalPriceNumber =  +orderSummary.itemsCost + +orderSummary.deliveryCost;
    totalPrice.innerHTML = "Items cost: " + orderSummary.itemsCost + "  Delivery cost: " + orderSummary.deliveryCost +
        "  Total price: " + totalPriceNumber;
    totalPriceDiv.appendChild(totalPrice);
    footer.appendChild(confirm);
    footer.appendChild(cancel);
}

function ajaxGetOrderSummary() {
    $('#orderHeader')[0].textContent = "Order confirmation";
    var urlParams = new URLSearchParams(window.location.search);
    if($("#staticOrder")[0].checked) {
        $.ajax({
            url: "../../orderSummary",
            data: {
                "items": JSON.stringify(itemsInOrder[storeId]),
                "zone": urlParams.get("zone"),
                "storeId": storeId,
                "deliveryDate": $("#deliveryDate")[0].value,
                "xLocation": $("#xLocation")[0].value,
                "yLocation": $("#yLocation")[0].value
            },
            success: function (orderSummary) {
                showStaticOrderSummary(orderSummary);
            },
            error: function (message) {
                alert(message);
            }
        })
    }
    else{
        var objectFromArr = toObject(itemsInOrder);
        $.ajax({
            url:"../../dynamicOrderSummary",
            method:"POST",
            data:{"itemsFromStores": JSON.stringify(objectFromArr),
                "zone": urlParams.get("zone"),
                "deliveryDate": $("#deliveryDate")[0].value,
                "xLocation": $("#xLocation")[0].value,
                "yLocation": $("#yLocation")[0].value
            },
            success:function (orderSummary) {
                showDynamicOrderSummary(JSON.parse(orderSummary));
            },
            error: function (message) {
                alert(message)
            }
        })
    }
}

var addDiscountsToOrder = function() {
    return function () {
        var discountsDiv = $("#orderDynamicFields")[0].childNodes;
        var discountStoreId;
        for(var i = 0 ; i<discountsDiv.length ; i++){
            if(discountsDiv[i].childNodes[0].checked && discountsDiv[i].childNodes[6] !== undefined && discountsDiv[i].childNodes[4].value === ""){
                alert("ERROR. discount selection not completed.");
                return;
            }
        }

        for(var i = 0 ; i<discountsDiv.length ; i++){
            if(discountsDiv[i].childNodes[0].checked){
                if(discountsDiv[i].childNodes[6] !== undefined){
                    var dropdown = discountsDiv[i].childNodes[4];
                    var selectedDiscountIndex = dropdown.selectedIndex;
                    discountStoreId = discountsDiv[i].childNodes[5].textContent;
                    itemsInOrder[discountStoreId][itemsInOrder[discountStoreId].length] = {
                        "itemId" : availableDiscounts[i].benefit.offer[selectedDiscountIndex].itemId,
                        "price" : availableDiscounts[i].benefit.offer[selectedDiscountIndex].forAdditional,
                        "quantity" : availableDiscounts[i].benefit.offer[selectedDiscountIndex].quantity,
                        "purchasedFromDiscount": true
                    }
                }
                else{
                    discountStoreId = discountsDiv[i].childNodes[4].textContent;
                    for(var j = 0; j< availableDiscounts[i].benefit.offer.length ; j++){
                        itemsInOrder[discountStoreId][itemsInOrder[discountStoreId].length] = {
                            "itemId" : availableDiscounts[i].benefit.offer[j].itemId,
                            "price" : availableDiscounts[i].benefit.offer[j].forAdditional,
                            "quantity" : availableDiscounts[i].benefit.offer[j].quantity,
                            "purchasedFromDiscount": true
                        }
                    }
                }
            }
        }

        ajaxGetOrderSummary();
    };
}

function offerDiscounts(discounts) {
    $('#orderHeader')[0].textContent = "Discounts";
    var discountsDiv = $('#orderDynamicFields')[0];
    for(var j = 0, i = availableDiscounts.length ; j<discounts.length ; i++, j++) {
        availableDiscounts[i] = discounts[j];
    }
    $.each(discounts || [], function (index1, discount) {
        var checkBox = document.createElement("input");
        var discountName = document.createElement("label");
        var discountDetails = document.createElement("div");
        var paragraph = document.createElement("p");
        var discountJson = document.createElement("p")
        var storeIdParagraph = document.createElement("p");
        var separator = document.createElement("hr");
        storeIdParagraph.innerText = storeId;
        storeIdParagraph.hidden = true;
        discountJson.classList.add("discountJson");
        discountJson.hidden = true;
        checkBox.setAttribute("type","checkbox");
        checkBox.id = index1;
        checkBox.classList.add("checkbox")
        checkBox.classList.add("checkbox-circle")
        discountName.setAttribute("for",index1);
        discountName.textContent = discount.name
        paragraph.innerHTML  +=  "<br> Because you bought " +discount.condition.quantity + " of " + discount.condition.itemName +
            " <br><br>You deserve " + discount.benefit.operator + "<br>";
        discountJson.innerText = JSON.stringify(discount);
        discountDetails.appendChild(checkBox);
        discountDetails.appendChild(discountName);
        discountDetails.appendChild(paragraph);
        discountDetails.appendChild(discountJson);

        if(discount.benefit.operator === "ONE-OF"){
            checkBox.onclick = enableDropdown();
            var dropdown = document.createElement("select");
            $.each(discount.benefit.offer || [], function (index2, offer) {
                var option = document.createElement("option");
                option.text = offer.quantity + " of " + offer.name + " for " + offer.forAdditional + " each";
                dropdown.appendChild(option);
            })
            discountDetails.appendChild(dropdown);
            dropdown.value = "";
            dropdown.disabled = true;
        }
        else {
            checkBox.onclick = reCalculateAvailableDiscounts();
            $.each(discount.benefit.offer || [], function (index2, offer) {
                paragraph.innerHTML += offer.quantity + " of " + offer.name + " for " + offer.forAdditional + " each<br>";
            })
        }
        discountDetails.appendChild(storeIdParagraph);
        discountDetails.appendChild(separator);
        discountsDiv.appendChild(discountDetails);
    })

}

function ajaxGetPossibleDiscounts(selectedItems) {
    var urlParams = new URLSearchParams(window.location.search);
    itemsInOrder[storeId] = []
    itemsInOrder[storeId] = selectedItems;
    $.ajax({
        url: "../../getPossibleDiscounts",
        async: false,
        data: {"zone" : urlParams.get("zone"), "storeId": storeId, "items": JSON.stringify(selectedItems)},
        success: function (discounts) {
            if(discounts.length > 0){
                offerDiscounts(discounts);
                thereAreDiscounts = true;
            }
        },
        error: function (message) {
            alert(message);
        }
    })
}

function ajaxGetStoreDetails(storeId) {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url:"../../storeDetails",
        data: {"zone": urlParams.get("zone"),"storeId": storeId},
        success: function (store) {
            var distance = Math.sqrt(Math.pow(store.location.x - +$("#xLocation")[0].value, 2) +
                Math.pow(store.location.y - +$("#yLocation")[0].value, 2)).toFixed(2);
            $("#storeName"+storeId)[0].innerHTML = "Store name: " + store.name+"<br>";
            $("#storeLocation"+storeId)[0].innerHTML = "Store location: [" + store.location.x + ","+ store.location.y + "]"+"<br>";
            $("#distance"+storeId)[0].innerHTML = "Distance from store: "+ distance +"<br>";
            $("#deliveryPpk" + storeId)[0].innerHTML = "Delivery price per kilometer: " + store.deliveryPpk+"<br>";
            $("#deliveryCost"+storeId)[0].innerHTML = "Delivery cost: " + distance * +store.deliveryPpk  +"<br>";
        },
        error: function () {

        }
    })
}

var offerDynamicOrderDiscounts = function(cheapestStores) {
    return function () {
        $("#orderDynamicFields").empty();
        thereAreDiscounts = false;
        $.each(cheapestStores || [] , function(index, storeItems){
            for(var i = 0;i<storeItems.length;i++){
                storeItems[i].itemId = storeItems[i].orderedItem.itemId;
            }

            storeId = index;
            ajaxGetPossibleDiscounts(storeItems);
        })

        if(thereAreDiscounts){
            var nextButton = document.createElement("button");
            nextButton.classList.add("modalNextButton");
            nextButton.classList.add("btn");
            nextButton.classList.add("btn-secondary");
            nextButton.textContent = "Next";
            nextButton.onclick = addDiscountsToOrder();
            $('#orderDynamicFields')[0].appendChild(nextButton);
        }
        else{
            ajaxGetOrderSummary();
        }
    };
}

function showCheapestStores(cheapestStores) {
    $("#orderDynamicFields").empty();
    $('#orderHeader')[0].textContent = "The order will be placed from the following stores";
    var nextButton = document.createElement("button")
    nextButton.textContent = "Next";
    nextButton.classList.add("modalNextButton");
    nextButton.classList.add("btn");
    nextButton.classList.add("btn-secondary");
    nextButton.onclick = offerDynamicOrderDiscounts(cheapestStores);
    $.each(cheapestStores || [], function (storeId, storeItems) {
        var separator = document.createElement("hr");
        var storeIdLabel = document.createElement("p")
        storeIdLabel.innerHTML = "<strong>Store ID: " + storeId +"</strong<br>";
        $("#orderDynamicFields")[0].appendChild(storeIdLabel)

        var storeNameLabel = document.createElement("p");
        storeNameLabel.id = "storeName"+storeId;
        $("#orderDynamicFields")[0].appendChild(storeNameLabel)

        var storeLocationLabel = document.createElement("p");
        storeLocationLabel.id = "storeLocation"+storeId;
        $("#orderDynamicFields")[0].appendChild(storeLocationLabel)

        var distanceFromStoreLabel = document.createElement("p");
        distanceFromStoreLabel.id = "distance"+storeId;
        $("#orderDynamicFields")[0].appendChild(distanceFromStoreLabel)

        var deliveryPpkLabel = document.createElement("p");
        deliveryPpkLabel.id = "deliveryPpk"+storeId;
        $("#orderDynamicFields")[0].appendChild(deliveryPpkLabel)

        var deliveryCostLabel = document.createElement("p");
        deliveryCostLabel.id = "deliveryCost"+storeId;
        $("#orderDynamicFields")[0].appendChild(deliveryCostLabel)

        var numberOfItemsLabel = document.createElement("p");
        numberOfItemsLabel.id = "numberOfItems"+storeId;
        numberOfItemsLabel.innerHTML = "Number of items types: " + storeItems.length + "<br>";
        $("#orderDynamicFields")[0].appendChild(numberOfItemsLabel)

        var itemsCostLabel = document.createElement("p");
        itemsCostLabel.id = "itemsCost"+storeId;
        $("#orderDynamicFields")[0].appendChild(itemsCostLabel)
        $("#orderDynamicFields")[0].appendChild(separator)

        ajaxGetStoreDetails(storeId);
        var itemsCost = 0;
        $.each(storeItems || [], function (index, item) {
            itemsCost+=item.totalPrice;
        })
        itemsCostLabel.innerHTML = "Items cost: " + itemsCost.toFixed(2) + "<br>"
    })
    $("#orderDynamicFields")[0].appendChild(nextButton);
}

function ajaxGetCheapestStores(selectedItems) {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../findCheapestStore",
        data: {"zone": urlParams.get("zone") , "items" : JSON.stringify(selectedItems)},
        success: function (cheapestStores) {
            showCheapestStores(cheapestStores);
        },
        error: function (message) {
            alert(message);
        }
    })
}

var getSelectedItems = function() {
    return function () {
        var tableRows = $('#itemsTable>tbody')[0].childNodes;
        var selectedItems = [];
        var i = 0;
        $.each(tableRows || [], function (rowIndex, row) {

            if ($("#staticOrder")[0].checked === true && row.childNodes[4].childNodes[0].value !== "" && row.childNodes[4].childNodes[0].value !== "0") {
                selectedItems[i] = {
                    "itemId": row.childNodes[0].childNodes[0].data,
                    "price": row.childNodes[3].childNodes[0].data,
                    "quantity": row.childNodes[4].childNodes[0].value,
                    "purchasedFromDiscount": false
                };
                i++;
            }
            else if($("#staticOrder")[0].checked !== true && row.childNodes[3].childNodes[0].value !== "" && row.childNodes[3].childNodes[0].value !== "0"){
                selectedItems[i] = {
                    "itemId": row.childNodes[0].childNodes[0].data,
                    "quantity": row.childNodes[3].childNodes[0].value,
                    "purchasedFromDiscount": false
                };
                i++;
            }
        })
        if(selectedItems.length === 0){
            alert("ERROR. you should select at least one item");
            return;
        }

        if($("#staticOrder")[0].checked === true) {
            $("#orderDynamicFields").empty();
            ajaxGetPossibleDiscounts(selectedItems);
            if(thereAreDiscounts){
                var nextButton = document.createElement("button");
                nextButton.textContent = "Next";
                nextButton.classList.add("modalNextButton");
                nextButton.classList.add("btn");
                nextButton.classList.add("btn-secondary");
                nextButton.onclick = addDiscountsToOrder();
                $('#orderDynamicFields')[0].appendChild(nextButton);
            }
            else{
                ajaxGetOrderSummary();
            }
        }
        else{
            ajaxGetCheapestStores(selectedItems);
        }
    }
}

function selectStoreModal() {
    $('#orderHeader')[0].textContent = "Select store";
    var orderDynamicFields = document.getElementById("orderDynamicFields");
    var storesDropDown = document.createElement("select");
    var deliveryCost = document.createElement("label");
    var deliveryCostDiv = document.createElement('div');
    var nextButtonDiv = document.createElement("div");
    var nextButton = document.createElement("button");
    nextButtonDiv.appendChild(nextButton);
    nextButton.textContent = "Next";
    nextButton.classList.add("modalNextButton");
    nextButton.classList.add("btn");
    nextButton.classList.add("btn-secondary");
    nextButton.addEventListener("click" , selectItems());
    deliveryCost.id = "deliveryCost";
    deliveryCostDiv.id = "deliveryCostDiv";
    deliveryCostDiv.appendChild(deliveryCost);
    storesDropDown.addEventListener("change",ajaxGetDeliveryCost(storesDropDown));
    storesDropDown.id = "storesDropDown";
    storesDropDown.innerHTML +='<br>'
    orderDynamicFields.appendChild(storesDropDown);
    orderDynamicFields.appendChild(deliveryCostDiv);
    orderDynamicFields.appendChild(nextButtonDiv);
    ajaxZoneStore();
}

function addStoresToDropDown(stores) {
    var storesDropDown = document.getElementById("storesDropDown");

    $.each(stores || [], function (index, store) {
        var option = document.createElement("option");
        option.text = store.id + " " + store.name + " " + "[" + store.location.x + "," + store.location.y + "]";
        storesDropDown.appendChild(option);
    })
}

function ajaxZoneStore() {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getZoneStores",
        data: {"zone": urlParams.get('zone')},
        success: function(stores){
            addStoresToDropDown(stores);
            $('#storesDropDown')[0].value = "";
        },
        error: function (message) {
            alert(message)
        }
    })
}

function ajaxIsValidLocation(xLocation, yLocation,callback) {
    var urlParams = new URLSearchParams(window.location.search);
    $("#orderMainPage")[0].hidden = true;
    $.ajax({
        url: "../../checkLocation",
        data: {"X" : xLocation, "Y" : yLocation, "zone" : urlParams.get('zone')},
        success: function (isValidLocation) {
            callback(isValidLocation);
        },
        error: function (message) {
        }

    })
}

var isInteger = function (event) {
    return function (event) {
        if(event.key < '0' || event.key > '9'){
            event.preventDefault();
        }
    }
}

function selectStoreOrItems(isValidLocation) {
    if (isValidLocation === "true") {
        if ($("#staticOrder")[0].checked === true) {
            selectStoreModal();
        } else {
            selectItems().call();
        }
    }
}

var newOrderMainPageNext = function () {
    return function () {
        var isStatic = $("#staticOrder")[0].checked;
        var isDynamic = $("#dynamicOrder")[0].checked;
        var date = $("#deliveryDate")[0].value;
        var xLocation = $("#xLocation")[0].value;
        var yLocation = $("#yLocation")[0].value;
        if (isStatic === false && isDynamic === false) {
            alert("ERROR. select order type");
            return;
        }
        if(date === ""){
            alert("error empty date");
            return;
        }
        if(xLocation === "" || yLocation === ""){
            alert("error empty location");
            return;
        }

        ajaxIsValidLocation(xLocation, yLocation,selectStoreOrItems);
    }
}

$(function () {
    document.getElementById("xLocation").addEventListener("keypress", isInteger(event));
    document.getElementById("yLocation").addEventListener("keypress", isInteger(event));
    $("#newOrderMainPage")[0].onclick = newOrderMainPageNext();
    $('#orderModal').on('hidden.bs.modal', function (e) {
        $("#orderMainPage")[0].hidden = false;
        $("#orderDynamicFields").empty();
        $("#staticOrder")[0].checked = false;
        $("#dynamicOrder")[0].checked = false;
        $("#deliveryDate")[0].value = "";
        $("#xLocation")[0].value = "";
        $("#yLocation")[0].value = "";
        storeId = undefined;
        itemsInOrder = [];
        availableDiscounts = [];
        thereAreDiscounts = false;
        $('#orderHeader')[0].textContent = "Make new order";
        $('#headerMessage')[0].textContent = "";
    })

})