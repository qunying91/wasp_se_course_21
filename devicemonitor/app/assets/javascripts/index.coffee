$ ->
  $.get "/devices", (devices) ->
    $("#devices").append "<tr><td>Device Id</td><td>Device name<td></tr>"
    $.each devices, (index, device) ->
      $("#devices").append "<tr><td>"+device.id+ "</td><td>" + device.customerId + "<td></tr>"