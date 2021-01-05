function getParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}
isAdmin = false;

function login() {
    user = document.getElementById("user").value;
    pass = document.getElementById("pass").value;

    $.ajax({
        type: "POST",
        url: "/api/web/login/",
        data: JSON.stringify({
            "username": user,
            "password": pass
        }),
        success: (resp) => {
            isAdmin = resp.admin;
            accessToken = resp.token;
            updateView();
        },
        error: () => {
            alert(":c so sad, wrong pass.")
        },
        contentType: "application/json"
    });
}

function updateView() {
    // is admin?

    if (!isAdmin) {

        $.ajax({
            type: "POST",
            url: "/api/web/validateToken/",
            data: JSON.stringify({
                "token": accessToken,
                "resourceId": resourceId
            }),
            success: (resp) => {
                document.getElementById("loginView").style = "display:none";
                document.getElementById("userView").style = "display:block";
            },
            error: () => {
                alert("Invalid or expired session.")
            },
            contentType: "application/json"
        });
    } else {
        $.ajax({
            type: "POST",
            url: "/api/web/validateToken/",
            data: JSON.stringify({
                "token": accessToken
            }),
            success: (resp) => {
                document.getElementById("loginView").style = "display:none";
                document.getElementById("adminView").style = "display:block";
                updateAdminList();
            },
            error: () => {
                alert("Invalid or expired session.")
            },
            contentType: "application/json"
        });

    }

}

accessToken = getParameterByName("token");
resourceId = getParameterByName("resourceId");
if (accessToken) {
    document.getElementById("loginView").style = "display:none";
    updateView();
}

function loadUserData() {
    start = new Date($("#start").val()).getTime() / 1000;
    end = new Date($("#end").val()).getTime() / 1000;

    $.ajax({
        type: "POST",
        url: "/api/web/getPositions/",
        data: JSON.stringify({
            "token": accessToken,
            "timestampStart": start,
            "timestampEnd": end
        }),
        success: (resp) => {
            if (resp.length == 0) {
                alert("No data logged yet for this device in the selected interval!");
                return;
            }
            map = new google.maps.Map(document.getElementById("map"), {
                center: {
                    lat: resp[0].latitude,
                    lng: resp[0].longitude
                },
                zoom: 8,
            });
            for (let i = 0; i < resp.length; i++) {
                marker = new google.maps.Marker({
                    position: {
                        lat: resp[i].latitude,
                        lng: resp[i].longitude
                    },
                });
                marker.setMap(map);
            }


        },
        error: () => {
            alert("Invalid or expired session.")
        },
        contentType: "application/json"
    });
}

function updateAdminList() {
    $.ajax({
        type: "POST",
        url: "/api/web/getDevices/",
        data: JSON.stringify({
            "token": accessToken
        }),
        success: (resp) => {
            for (let i = 0; i < resp.length; i++) {
                $("#adminList").append(new Option(resp[i], resp[i]));
            }
        },
        error: () => {
            alert("Invalid or expired session.")
        },
        contentType: "application/json"
    });

}

function loadAdminData() {
    start = new Date($("#startAdmin").val()).getTime() / 1000;
    end = new Date($("#endAdmin").val()).getTime() / 1000;

    $.ajax({
        type: "POST",
        url: "/api/web/getPositions/",
        data: JSON.stringify({
            "token": accessToken,
            "resourceID": $("#adminList").val(),
            "timestampStart": start,
            "timestampEnd": end
        }),
        success: (resp) => {
            if (resp.length == 0) {
                alert("No data logged yet for this device in the selected interval!");
                return;
            }
            map = new google.maps.Map(document.getElementById("mapAdmin"), {
                center: {
                    lat: resp[0].latitude,
                    lng: resp[0].longitude
                },
                zoom: 8,
            });
            for (let i = 0; i < resp.length; i++) {
                marker = new google.maps.Marker({
                    position: {
                        lat: resp[i].latitude,
                        lng: resp[i].longitude
                    },
                });
                marker.setMap(map);
            }


        },
        error: () => {
            alert("Invalid or expired session.")
        },
        contentType: "application/json"
    });
}