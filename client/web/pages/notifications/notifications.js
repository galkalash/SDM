
function startStoreOwnerNotifications() {
    setInterval(checkForNewOrders,1000);
    setInterval(checkForNewFeedbacks,1000);
    setInterval(checkForNewStore,1000);
}


function checkForNewStore() {
    $.ajax({
       url: "../../newStoreNotification",
       success:function (notifications) {
           if(notifications.length > 0) {
               $.each(notifications || [], function (index, notification) {
                   showNewStoreNotification(notification);
               })
           }
       }
   })
}

function showNewStoreNotification(notification) {
    $("#notifications")[0].innerHTML += "<div class=\"alert alert-warning alert-dismissible fade show notification\" role=\"alert\">\n" +
        "  <strong>New store added to : " + notification.zoneName +"</strong><br>" +
        "<strong>New store owner: </strong>" + notification.ownerName + "<br>"+
        "<strong>Store name: </strong>" + notification.storeName +"<br>"+
        "<strong>Location: </strong>" +"["+ notification.storeLocation.x+ ","+notification.storeLocation.y +"]"+ "<br>" +
        "<strong>Number of items being sold: </strong>" + notification.numberOfItemsInStore+ "/" + notification.numberOfItemsInZone +"<br>" +
        "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>";
}

function showNewOrderNotification(orderNotification){
    $("#notifications")[0].innerHTML += "<div class=\"alert alert-success alert-dismissible fade show notification\" role=\"alert\">\n" +
        "  <strong>New order received:</strong><br>" +
        "<strong>Order id: </strong>" + orderNotification.orderId + "<br>"+
        "<strong>Customer name: </strong>" + orderNotification.customerName +"<br>"+
        "<strong>Number of items types: </strong>" + orderNotification.numberOfItemsTypes+"<br>" +
        "<strong>Items cost: </strong>" + orderNotification.itemsCost+"<br>" +
        "<strong>Delivery cost: </strong>" + orderNotification.deliveryCost+
        "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>";
}

function checkForNewOrders(){
    if(sessionStorage.getItem("numberOfOrdersSeen") === null){
        sessionStorage.setItem("numberOfOrdersSeen",'0');
    }
    $.ajax({
        url: "../../getNewOrdersNotifications",
        data:{"ordersSeen" : sessionStorage.getItem("numberOfOrdersSeen")},
        success: function (newOrdersNotifications) {
            if(newOrdersNotifications.length > 0) {
                var ordersId = [];
                for (var i = 0; i < newOrdersNotifications.length; i++) {
                    if(ordersId[newOrdersNotifications[i].orderId] === undefined){
                        sessionStorage.setItem("numberOfOrdersSeen",+sessionStorage.getItem("numberOfOrdersSeen")+1);
                        ordersId[newOrdersNotifications[i].orderId] = newOrdersNotifications[i];
                    }
                    showNewOrderNotification(newOrdersNotifications[i]);
                }
            }
        }
    })
}

function showNewFeedbackNotification(feedbackNotification) {
    if(feedbackNotification.feedback !== ""){
        $("#notifications")[0].innerHTML += "<div class=\"alert alert-info alert-dismissible fade show notification\" role=\"alert\">\n" +
            "  <strong>New feedback received:</strong><br>" +
            "<strong>Order id: </strong>" + feedbackNotification.orderId + "<br>"+
            "<strong>Customer name: </strong>" + feedbackNotification.customerName +"<br>"+
            "<strong>Rating: </strong>" + feedbackNotification.rating+"<br>" +
            "<strong>Feedback: </strong>" + feedbackNotification.feedback +
            "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
        "    <span aria-hidden=\"true\">&times;</span>\n" +
        "  </button>\n" +
        "</div>";
    }
    else{
        $("#notifications")[0].innerHTML += "<div class=\"alert alert-info alert-dismissible fade show notification\" role=\"alert\">\n" +
            "  <strong>New feedback received:</strong><br>" +
            "<strong>Order id: </strong>" + feedbackNotification.orderId + "<br>"+
            "<strong>Customer name: </strong>" + feedbackNotification.customerName +"<br>"+
            "<strong>Rating: </strong>" + feedbackNotification.rating+"<br>" +
            "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">\n" +
            "    <span aria-hidden=\"true\">&times;</span>\n" +
            "  </button>\n" +
            "</div>";
    }
}

function checkForNewFeedbacks(){
    if(sessionStorage.getItem("numberOfFeedbacksSeen") === null){
        sessionStorage.setItem("numberOfFeedbacksSeen",'0');
    }
    $.ajax({
        url: "../../getNewFeedbacksNotifications",
        data:{"feedbacksSeen" : sessionStorage.getItem("numberOfFeedbacksSeen")},
        success: function (newFeedbacksNotifications) {
            if(newFeedbacksNotifications.length > 0) {
                sessionStorage.setItem("numberOfFeedbacksSeen",+sessionStorage.getItem("numberOfFeedbacksSeen")+newFeedbacksNotifications.length);
                for (var i = 0; i < newFeedbacksNotifications.length; i++) {
                    showNewFeedbackNotification(newFeedbacksNotifications[i]);
                }
            }
        }
    })
}