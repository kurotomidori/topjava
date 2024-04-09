$.datetimepicker.setLocale(window.navigator.language);

$('#dateTime').datetimepicker({format: 'Y-m-d H:i'});

let startDate = $('#startDate');
let endDate = $('#endDate');
let startTime = $('#startTime');
let endTime = $('#endTime');


$('#startDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    onShow: function (currentDateTime) {
        this.setOptions({
            maxDate: endDate.val() ? endDate.val() : false
        });
    }
});

$('#endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d',
    onShow: function (currentDateTime) {
        this.setOptions({
            minDate: startDate.val() ? startDate.val() : false
        });
    }
});

$('#startTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
    onShow: function (currentDateTime) {
        this.setOptions({
            maxTime: endTime.val() ? endTime.val() : false
        });
    }
});

$('#endTime').datetimepicker({
    datepicker: false,
    format: 'H:i',
    onShow: function (currentDateTime) {
        this.setOptions({
            minTime: startTime.val() ? startTime.val() : false
        });
    }
});