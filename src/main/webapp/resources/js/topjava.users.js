const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        redrawDataTable(data);
    });
}

function setActiveStatus(id) {
    let status = $('#enabled_' + id)[0].checked;
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + id,
        data: {"id": id, "status": status}
    }).done(function () {
        successNoty(status ? "Enabled user" : "Disabled user");
        $('#' + id).attr("is-enabled", status);
    });
}