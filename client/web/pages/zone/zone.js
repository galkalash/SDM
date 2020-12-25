
function showZoneItems(items) {
    var tableBody = document.getElementById("itemsTableRows");
    $('#itemsTableRows').empty();

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

        var numberOfStoresSellingThisItemCell = document.createElement("td");
        var numberOfStoresSellingThisItemCellText = document.createTextNode(item.numberOfStoresThatSellingThisItem);
        numberOfStoresSellingThisItemCell.appendChild(numberOfStoresSellingThisItemCellText);
        row.appendChild(numberOfStoresSellingThisItemCell);

        var averagePriceCell = document.createElement("td");
        var averagePriceCellText = document.createTextNode(item.averagePrice);
        averagePriceCell.appendChild(averagePriceCellText);
        row.appendChild(averagePriceCell);

        var numberOfTimesSoldCell = document.createElement("td");
        var numberOfTimesSoldCellText = document.createTextNode(item.numberOfTimesSold);
        numberOfTimesSoldCell.appendChild(numberOfTimesSoldCellText);
        row.appendChild(numberOfTimesSoldCell);

        tableBody.appendChild(row);
    });
}

function ajaxZoneItems(callback) {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getZoneItems",
        data: {"zone": urlParams.get('zone')},
        success: function(items){
            callback(items);
        },
        error: function (message) {
            alert(message)
        }
    })
}

function backToStoreOrders() {
    $('#storeItemsInOrderTable').hide();
    $('#storeItemsInOrderRows').empty();
    $('#storeOrdersTable').show();
    $('#ordersHeader').show();
    $('#itemsInOrderHeader').hide();
    $('#backToItemsButton')[0].onclick = backToStoreItems;
}

function showItemsInOrder(items, orderSerialNumber) {
    return function () {
        $('#storeItemsInOrderTable').show();
        $('#storeOrdersTable').hide();
        $('#ordersHeader').hide();
        $('#itemsInOrderHeader').show();
        $('#backToItemsButton')[0].onclick = backToStoreOrders;

        $('#itemsInOrderHeader')[0].innerText = "Items in order " + orderSerialNumber;
        var tblBody = document.getElementById("storeItemsInOrderRows");
        $.each(items || [], function (index, item) {
            var row = document.createElement("tr");

            var idCell = document.createElement("td");
            var idCellText = document.createTextNode(item.orderedItem.itemId);
            idCell.appendChild(idCellText);
            row.appendChild(idCell);

            var nameCell = document.createElement("td");
            var nameCellText = document.createTextNode(item.orderedItem.name);
            nameCell.appendChild(nameCellText);
            row.appendChild(nameCell);

            var purchaseTypeCell = document.createElement("td");
            var purchaseTypeCellText = document.createTextNode( item.orderedItem.purchaseType);
            purchaseTypeCell.appendChild(purchaseTypeCellText);
            row.appendChild(purchaseTypeCell);


            var quantityCell = document.createElement("td");
            var quantityCellText = document.createTextNode(item.quantity);
            quantityCell.appendChild(quantityCellText);
            row.appendChild(quantityCell);

            var priceCell = document.createElement("td");
            var priceCellText =  document.createTextNode(item.price);
            priceCell.appendChild(priceCellText);
            row.appendChild(priceCell);

            var totalPriceCell = document.createElement("td");
            var totalPriceCellText =  document.createTextNode(item.totalPrice);
            totalPriceCell.appendChild(totalPriceCellText);
            row.appendChild(totalPriceCell);

            var purchasedFromDiscountCell = document.createElement("td");
            var purchasedFromDiscountCellText;
            if(item.isPurchaseWithDiscount){
                purchasedFromDiscountCellText =  document.createTextNode('V');
            }
             else{
                purchasedFromDiscountCellText =  document.createTextNode('X');
            }
            purchasedFromDiscountCell.appendChild(purchasedFromDiscountCellText);
            row.appendChild(purchasedFromDiscountCell);

            tblBody.appendChild(row);
        });

    };
}

var ajaxStoreOrdersHistory = function(storeId, storeName) {
    return function() {
        $('#storeItemsTable').hide();
        $('#storeOrdersTable').show();
        $('#backToItemsButton').show();
        $('#showOrdersButton').hide();
        $('#itemsHeader').hide();
        $('#ordersHeader').show();

        $('#ordersHeader')[0].innerText = "Orders from " + storeName +"`s store";
        var urlParams = new URLSearchParams(window.location.search);
        $.ajax({
            url:"../../storeOrdersHistory",
            data:{"zone":urlParams.get("zone"), "storeId":storeId},
            success: function (orders) {
                var tblBody = document.getElementById("storeOrdersRows");

                $.each(orders || [], function (index, order) {
                    var row = document.createElement("tr");
                    row.onclick = showItemsInOrder(order.orderDetails.items, order.serialNumber);

                    var serialCell = document.createElement("td");
                    var serialCellText = document.createTextNode(order.serialNumber);
                    serialCell.appendChild(serialCellText);
                    row.appendChild(serialCell);

                    var dateCell = document.createElement("td");
                    var dateCellText = document.createTextNode(order.orderDate);
                    dateCell.appendChild(dateCellText);
                    row.appendChild(dateCell);

                    var customerCell = document.createElement("td");
                    var customerCellText = document.createTextNode( order.customerName +" [" + order.customerLocation.x + "," + order.customerLocation.y + "]");
                    customerCell.appendChild(customerCellText);
                    row.appendChild(customerCell);


                    var numberOfItemsCell = document.createElement("td");
                    var numberOfItemsCellText = document.createTextNode(order.orderDetails.numberOfItems);
                    numberOfItemsCell.appendChild(numberOfItemsCellText);
                    row.appendChild(numberOfItemsCell);

                    var itemsCostCell = document.createElement("td");
                    var itemsCostCellText =  document.createTextNode(order.orderDetails.itemsCost);
                    itemsCostCell.appendChild(itemsCostCellText);
                    row.appendChild(itemsCostCell);

                    var deliveryCostCell = document.createElement("td");
                    var deliveryCostCellText =  document.createTextNode(order.orderDetails.deliveryCost);
                    deliveryCostCell.appendChild(deliveryCostCellText);
                    row.appendChild(deliveryCostCell);

                    tblBody.appendChild(row);
                });
            }
        })
    }
}

function backToStoreItems() {
    $('#storeItemsTable').show();
    $('#storeOrdersTable').hide();
    $('#backToItemsButton').hide();
    $('#showOrdersButton').show();
    $('#storeOrdersRows').empty();
    $('#itemsHeader').show();
    $('#ordersHeader').hide();
}

function showItemsModal(items,store) {
    var tableBody = document.getElementById("storeItemsRows");
    $("#storeItemsRows").empty();
    $("#itemsHeader").text("The following items are being sold by " + store.name +"`s store:");
    var showOrdersButton = document.createElement("button");
    if(sessionStorage.getItem("username") === store.ownerName) {
        showOrdersButton.id = "showOrdersButton";
        showOrdersButton.classList.add("btn")
        showOrdersButton.classList.add("btn-secondary")
        showOrdersButton.classList.add("modalButtonsDiv")
        showOrdersButton.onclick = ajaxStoreOrdersHistory(store.id, store.name);
        showOrdersButton.textContent = "Show orders";
        $("#storeItemsDiv")[0].appendChild(showOrdersButton);
    }

    $.each(JSON.parse(items) || [], function (index, item) {
        var row = document.createElement("tr");

        var itemIdCell = document.createElement("td");
        var itemIdCellText = document.createTextNode(item.id);
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

        var priceCell = document.createElement("td");
        var priceCellText = document.createTextNode(item.price);
        priceCell.appendChild(priceCellText);
        row.appendChild(priceCell);

        var numberOfTimesSoldCell = document.createElement("td");
        var numberOfTimesSoldCellText = document.createTextNode(item.numberOfTimeSold);
        numberOfTimesSoldCell.appendChild(numberOfTimesSoldCellText);
        row.appendChild(numberOfTimesSoldCell);

        tableBody.appendChild(row);
    });
}

function ajaxGetStoreItems(store) {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getStoreItems",
        data: {"storeId": store.id, "zone": urlParams.get('zone')},
        success: function(items){
            showItemsModal(items,store);
        },
        error: function (message) {
            alert(message)
        }
    });
}

var shopClickEventHandler = function (store) {
    return function () {
        ajaxGetStoreItems(store);
    }
}

function showZoneStores(stores) {
    var tableBody = document.getElementById("storesTableRows");
    $('#storesTableRows').empty();

    $.each(stores || [], function (index, store) {
        var row = document.createElement("tr");
        row.dataset.target = "#storeItemsModal";
        row.dataset.toggle = "modal";
        row.onclick = shopClickEventHandler(store);

        var storeIdCell = document.createElement("td");
        var storeIdCellText = document.createTextNode(store.id);
        storeIdCell.appendChild(storeIdCellText);
        row.appendChild(storeIdCell);

        var storeNameCell = document.createElement("td");
        var storeNameCellText = document.createTextNode(store.name);
        storeNameCell.appendChild(storeNameCellText);
        row.appendChild(storeNameCell);

        var ownerCell = document.createElement("td");
        var ownerCellText = document.createTextNode(store.ownerName);
        ownerCell.appendChild(ownerCellText);
        row.appendChild(ownerCell);

        var numberOfOrdersCell = document.createElement("td");
        var numberOfOrdersCellText = document.createTextNode(store.numberOfOrders);
        numberOfOrdersCell.appendChild(numberOfOrdersCellText);
        row.appendChild(numberOfOrdersCell);

        var paymentReceivedForItemsCell = document.createElement("td");
        var paymentReceivedForItemsCellText = document.createTextNode(store.totalPaymentForItems);
        //ajaxPaymentFromItems(store.id,paymentReceivedForItemsCellText)
        paymentReceivedForItemsCell.appendChild(paymentReceivedForItemsCellText);
        row.appendChild(paymentReceivedForItemsCell);

        var deliveryPpkCell = document.createElement("td");
        var deliveryPpkCellText = document.createTextNode(store.deliveryPricePerKilometer);
        deliveryPpkCell.appendChild(deliveryPpkCellText);
        row.appendChild(deliveryPpkCell);

        var paymentReceivedForDeliveriesCell = document.createElement("td");
        var paymentReceivedForDeliveriesCellText = document.createTextNode(store.totalPaymentForDeliveries);
        paymentReceivedForDeliveriesCell.appendChild(paymentReceivedForDeliveriesCellText);
        row.appendChild(paymentReceivedForDeliveriesCell);

        tableBody.appendChild(row);
    });
}

function ajaxPaymentFromItems(storeId,paymentReceivedForItemsCellText){
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getPaymentFromItems",
        data: {"storeId": storeId, "zone": urlParams.get('zone')},
        success: function(paymentReceived){
            paymentReceivedForItemsCellText.data = paymentReceived;
        },
        error: function (message) {
            alert(message)
        }
    })
}

function ajaxZoneStores() {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getZoneStores",
        data: {"zone": urlParams.get('zone')},
        success: function(stores){
            showZoneStores(stores);
        },
        error: function (message) {
            alert(message)
        }
    })
}

function ajaxUserData() {
    $.ajax({
        url: "../../getUserData",
        success: function(user) {
            if(user['stores'] !== undefined){
                $('#newOrderButton').hide();
                $('#ordersHistoryButton').hide();
                $('#showFeedbacksButton').show();
                $('#addNewStoreButton').show();
                startStoreOwnerNotifications();
            }
            if(sessionStorage.getItem("username") === null){
                sessionStorage.setItem("username", user.name);
            }
        },
        error:function () {
            console.log("error");
        }
    });
}

function addStoreNameToStaticOrderItemsTable(storeName, tableBody) {
    $.each(tableBody.childNodes || [] , function (index, tableRow) {
        tableRow.childNodes[3].textContent += " " + storeName;
    })
}

function addStoreNameToDynamicOrderItemsTable(storeName, tableBody, storeId) {
    $.each(tableBody.childNodes || [] , function (index, tableRow) {
        if(tableRow.classList.contains(storeId)) {
            tableRow.childNodes[3].textContent += " " + storeName;
        }
    })
}

function ajaxGetStoreName(storeId, tableBody, callback) {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url:"../../storeDetails",
        data:{"zone": urlParams.get("zone"), "storeId":storeId},
        success: function (store) {
            callback(store.name, tableBody,storeId);
        }
    })
}

var getOrderDetails = function(event) {
    return function (event) {
        var urlParams = new URLSearchParams(window.location.search);
        var orderSerialNumber = event.target.parentElement.childNodes[0].textContent;
        $.ajax({
            url:"../../orderDetails",
            data: {"zone": urlParams.get("zone"), "serialNumber":orderSerialNumber},
            success: function (order) {
                $('#ordersHistoryTable').hide();
                $('#orderItemsTable').show();
                $('#backButton').show();

                var tableBody = $('#orderItemsTableBody')[0];
                if(order.ordersFromStores === undefined){
                    $.each(order.orderDetails.items || [] , function (index, item) {
                        var row = document.createElement("tr");

                        var idCell = document.createElement("td");
                        var idCellText = document.createTextNode(item.orderedItem.itemId);
                        idCell.appendChild(idCellText);
                        row.appendChild(idCell);

                        var nameCell = document.createElement("td");
                        var nameCellText = document.createTextNode(item.orderedItem.name);
                        nameCell.appendChild(nameCellText);
                        row.appendChild(nameCell);

                        var purchaseTypeCell = document.createElement("td");
                        var purchaseTypeCellText = document.createTextNode(item.orderedItem.purchaseType);
                        purchaseTypeCell.appendChild(purchaseTypeCellText);
                        row.appendChild(purchaseTypeCell);

                        var storeCell = document.createElement("td");
                        var storeCellText = document.createTextNode(order.storeId);
                        storeCell.appendChild(storeCellText);
                        row.appendChild(storeCell);

                        var quantityCell = document.createElement("td");
                        var quantityCellText = document.createTextNode(item.quantity);
                        quantityCell.appendChild(quantityCellText);
                        row.appendChild(quantityCell);

                        var priceCell = document.createElement("td");
                        var priceCellText = document.createTextNode(item.price);
                        priceCell.appendChild(priceCellText);
                        row.appendChild(priceCell);

                        var totalPriceCell = document.createElement("td");
                        var totalPriceCellText = document.createTextNode(item.totalPrice);
                        totalPriceCell.appendChild(totalPriceCellText);
                        row.appendChild(totalPriceCell);

                        var purchasedFromDiscountCell = document.createElement("td");
                        var purchasedFromDiscountCellText;
                        if(item.isPurchaseWithDiscount){
                            purchasedFromDiscountCellText = document.createTextNode('V');
                        }
                        else{
                            purchasedFromDiscountCellText = document.createTextNode('X');
                        }
                        purchasedFromDiscountCell.appendChild(purchasedFromDiscountCellText);
                        row.appendChild(purchasedFromDiscountCell);

                        tableBody.appendChild(row);
                    })

                    ajaxGetStoreName(order.storeId, tableBody, addStoreNameToStaticOrderItemsTable);
                }
                else{
                    $.each(order.ordersFromStores || [] , function (storeId, orderFromStore) {
                        $.each(orderFromStore.items || [],function (index2, item) {
                            var row = document.createElement("tr");
                            row.classList.add(storeId);

                            var idCell = document.createElement("td");
                            var idCellText = document.createTextNode(item.orderedItem.itemId);
                            idCell.appendChild(idCellText);
                            row.appendChild(idCell);

                            var nameCell = document.createElement("td");
                            var nameCellText = document.createTextNode(item.orderedItem.name);
                            nameCell.appendChild(nameCellText);
                            row.appendChild(nameCell);

                            var purchaseTypeCell = document.createElement("td");
                            var purchaseTypeCellText = document.createTextNode(item.orderedItem.purchaseType);
                            purchaseTypeCell.appendChild(purchaseTypeCellText);
                            row.appendChild(purchaseTypeCell);

                            var storeCell = document.createElement("td");
                            var storeCellText = document.createTextNode(storeId);
                            storeCell.appendChild(storeCellText);
                            row.appendChild(storeCell);

                            var quantityCell = document.createElement("td");
                            var quantityCellText = document.createTextNode(item.quantity);
                            quantityCell.appendChild(quantityCellText);
                            row.appendChild(quantityCell);

                            var priceCell = document.createElement("td");
                            var priceCellText = document.createTextNode(item.price);
                            priceCell.appendChild(priceCellText);
                            row.appendChild(priceCell);

                            var totalPriceCell = document.createElement("td");
                            var totalPriceCellText = document.createTextNode(item.totalPrice);
                            totalPriceCell.appendChild(totalPriceCellText);
                            row.appendChild(totalPriceCell);

                            var purchasedFromDiscountCell = document.createElement("td");
                            var purchasedFromDiscountCellText;
                            if(item.isPurchaseWithDiscount){
                                purchasedFromDiscountCellText = document.createTextNode('V');
                            }
                            else{
                                purchasedFromDiscountCellText = document.createTextNode('X');
                            }
                            purchasedFromDiscountCell.appendChild(purchasedFromDiscountCellText);
                            row.appendChild(purchasedFromDiscountCell);

                            tableBody.appendChild(row);
                        })
                        ajaxGetStoreName(storeId,tableBody, addStoreNameToDynamicOrderItemsTable);
                    })
                }
            }
        })
    };
}

function ajaxCustomerOrdersHistory() {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../userOrdersHistory",
        data: {"zone": urlParams.get("zone")},
        success: function (orders) {
            var tblBody = document.getElementById("ordersHistoryTableBody");

            $.each(orders || [], function (index, order) {
                var row = document.createElement("tr");
                row.onclick = getOrderDetails(row, event);


                var serialCell = document.createElement("td");
                var serialCellText = document.createTextNode(order.serialNumber);
                serialCell.appendChild(serialCellText);
                row.appendChild(serialCell);

                var dateCell = document.createElement("td");
                var dateCellText = document.createTextNode(order.orderDate);
                dateCell.appendChild(dateCellText);
                row.appendChild(dateCell);

                var customerLocationCell = document.createElement("td");
                var customerLocationCellText = document.createTextNode("[" + order.customerLocation.x + "," + order.customerLocation.y + "]");
                customerLocationCell.appendChild(customerLocationCellText);
                row.appendChild(customerLocationCell);

                var numberOfStoresCell = document.createElement("td");
                var numberOfStoresCellText;
                if(order.ordersFromStores === undefined){
                    numberOfStoresCellText = document.createTextNode("1");
                }
                else{
                    numberOfStoresCellText = document.createTextNode(Object.keys(order.ordersFromStores).length);
                }
                numberOfStoresCell.appendChild(numberOfStoresCellText);
                row.appendChild(numberOfStoresCell);

                var numberOfItemsCell = document.createElement("td");
                var numberOfItemsCellText;
                if(order.ordersFromStores === undefined){
                    numberOfItemsCellText = document.createTextNode(order.orderDetails.numberOfItems);
                }
                else{
                    numberOfItemsCellText = document.createTextNode(order.numberOfItemsInOrder);
                }
                numberOfItemsCell.appendChild(numberOfItemsCellText);
                row.appendChild(numberOfItemsCell);

                var itemsCostCell = document.createElement("td");
                var itemsCostCellText;
                if(order.ordersFromStores === undefined){
                    itemsCostCellText =  document.createTextNode(order.orderDetails.itemsCost);
                }
                else{
                    itemsCostCellText = document.createTextNode(order.itemsCost);
                }
                itemsCostCell.appendChild(itemsCostCellText);
                row.appendChild(itemsCostCell);

                var deliveryCostCell = document.createElement("td");
                var deliveryCostCellText;
                if(order.ordersFromStores === undefined){
                    deliveryCostCellText =  document.createTextNode(order.orderDetails.deliveryCost);
                }
                else{
                    deliveryCostCellText = document.createTextNode(order.deliveryCost);
                }
                deliveryCostCell.appendChild(deliveryCostCellText);
                row.appendChild(deliveryCostCell);

                var totalCostCell = document.createElement("td");
                var totalCostCellText = document.createTextNode(+deliveryCostCellText.textContent + +itemsCostCellText.textContent);
                totalCostCell.appendChild(totalCostCellText);
                row.appendChild(totalCostCell);
                tblBody.appendChild(row);
            });
        }
    })
}

function backToOrdersHistory(){
    $('#orderItemsTableBody').empty();
    $('#orderItemsTable').hide();
    $('#ordersHistoryTable').show();
    $('#backButton').hide();
}

function showFeedbacks() {
    var urlParams = new URLSearchParams(window.location.search);
    $.ajax({
        url: "../../getFeedbacks",
        data: {"zone": urlParams.get("zone"), "storeOwner": sessionStorage.getItem("username")},
        success: function (feedbacks) {
            var tableBody = $('#feedbacksTableBody')[0];
            $.each(feedbacks || [] , function (index, feedback) {
                var row = document.createElement("tr");

                var customerCell = document.createElement("td");
                var customerCellText = document.createTextNode(feedback.customerName);
                customerCell.appendChild(customerCellText);
                row.appendChild(customerCell);

                var orderDateCell = document.createElement("td");
                var orderDateCellText = document.createTextNode(feedback.date);
                orderDateCell.appendChild(orderDateCellText);
                row.appendChild(orderDateCell);

                var ratingCell = document.createElement("td");
                var ratingCellText = document.createTextNode(feedback.rating);
                ratingCell.appendChild(ratingCellText);
                row.appendChild(ratingCell);

                var feedbackCell = document.createElement("td");
                var feedbackCellText = document.createTextNode(feedback.feedback);
                feedbackCell.appendChild(feedbackCellText);
                row.appendChild(feedbackCell);

                tableBody.appendChild(row);
            })
        }
    })
}




$(function () {
    ajaxUserData();
    setInterval(function () {ajaxZoneItems(showZoneItems)},1000);
    setInterval(ajaxZoneStores,1000);

    $('#ordersHistoryModal').on('hidden.bs.modal', function (e) {
        $('#ordersHistoryTableBody').empty();
        backToOrdersHistory();
    })
    $('#storeItemsModal').on('hidden.bs.modal', function (e) {
        if($('#storeItemsDiv')[0].contains($('#showOrdersButton')[0])){
            $('#storeItemsDiv')[0].removeChild($('#showOrdersButton')[0]);
        }
        backToStoreOrders();
        backToStoreItems();
    })
})