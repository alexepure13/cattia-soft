$(document).ready(
    function() {
        if(alertVisible == 1){
            if(alertType=="success"){
                toastr.success(alertMessage);
            }else if(alertType=="info"){
                toastr.info(alertMessage);
            }else if(alertType=="error"){
                toastr.error(alertMessage);
            }else if(alertType=="warning"){
                toastr.warning(alertMessage);
            }
        }
    }
);
