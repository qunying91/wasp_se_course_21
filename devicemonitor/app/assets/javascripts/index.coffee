$ ->
  $.get "/devices", (devices) ->
    $("#devices").append "<tr><th>DEVICE ID</th><th>DESCRIPTION</th><th>CUSTOMER ID</th><th>LAST UPDATE TIME</th><th>STATUS</th><th>DETAILS</th><th>OPTION</th></tr>"
    $.each devices, (index, device) ->
      date = new Date(device.updateAt)
      if device.status is 'ACTIVE'
        $("#devices").append "<tr><td>"+device.id+ "</td><td>" + device.description + "</td><td>" + device.customerId +
        "</td><td>" + date.toString() + "</td><td>" + device.status + "</td><td><button class=\"buttonGreen\">Check</button></td><td><button class=\"buttonGrey\">Remove</button></td></tr>"
      else
        $("#devices").append "<tr><td>"+device.id+ "</td><td>" + device.description + "</td><td>" + device.customerId +
        "</td><td>" + date.toString() + "</td><td>" + device.status + "</td><td><button class=\"buttonRed\">Check</button></td><td><button class=\"buttonGrey\">Remove</button></td></tr>"
