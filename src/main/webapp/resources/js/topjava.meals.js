const mealAjaxUrl = "ajax/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
            "order": []
        })
    );
});

let isFilterEnabled = false;

function enableFilter() {
    isFilterEnabled = true;
    updateTable();
}

function disableFilter() {
    isFilterEnabled = false;
    $('#filterForm')[0].reset();
    updateTable();
}

function updateTable() {
    if (isFilterEnabled) {
        $.ajax({
            url: ctx.ajaxUrl + "filter",
            type: "GET",
            data: $('#filterForm').serialize()
        }).done(function (data) {
            redrawDataTable(data);
        });
    } else {
        $.get(ctx.ajaxUrl, function (data) {
            redrawDataTable(data);
        });
    }
}