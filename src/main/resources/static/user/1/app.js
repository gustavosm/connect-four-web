$(document).ready(function(){
    var boardStatus = 0;
    var count = 0;

    var finalStage = false;
    var alertMessage = '';

    jQuery.post({
        url: "http://localhost:8090/board/1",
        success: function(data) {
            boardStatus = data['boardStatus'];
            if (boardStatus == "ON_GOING") {
                setBackgroundColor(data['backgroundRGBAlphaArray'], ".grid");
                var cellList = data['cellList'];
                $(".cell").each(function(){
                    $(this).attr("id", cellList[count].cellId);
                    $(this).attr("data-player", (cellList[count].cellInUse ? 1 : 0));
                    setBackgroundColor(cellList[count].backgroundRGBAlphaArray, this);
                    $(this).click(function(){
                        performClick($(this).attr("id"));
                    });
                    $(this).mouseenter(function() {
                        if (!finalStage) {
                            if ($(this).attr("data-player") == 0) {
                                $(this).toggleClass("cell_hover", true);
                            }
                        }
                    });
                    $(this).mouseleave(function() {
                        if (!finalStage) {
                            if ($(this).attr("data-player") == 0) {
                                $(this).toggleClass("cell_hover", false);
                            }
                        }
                    });
                    count++;
                });
            } else {
                alertMessage = processBoardStatus(boardStatus);
                alert(alertMessage);
            }
        }
    });


    function setBackgroundColor(rgbArray, clss) {
        $(clss).css("background-color", 'rgb('+rgbArray[0]+','+rgbArray[1]+','+rgbArray[2]+')');
    };

    function performClick(cellId) {
        if (!finalStage) {
            var postData = {};
            postData["cellId"] = cellId;
            jQuery.post({
                url: "http://localhost:8090/board/play/1",
                data: JSON.stringify(postData),
                contentType: 'application/json',
                dataType: 'json',
                async: 'false',
                success: function(movement) {
                    
                    var humanCell = movement.humanChosenCell;
                    var botCell = movement.botChosenCell;
                    var boardStatus = movement.boardStatus;

                    refresh(humanCell);
                    refresh(botCell);

                    if (boardStatus !== "ON_GOING") {
                        finalStage = true;
                        alertMessage = processBoardStatus(boardStatus);
                        alert(alertMessage);
                    }
                    
                },
                error: function(response) {
                    alert("Bad Movement: " + response.responseJSON);
                }
            });
        } else {
            alert(alertMessage);
        }
        
    }

    function refresh(cell) {
        $("#" + cell.cellId).attr("data-player", "1");
        setBackgroundColor(cell.backgroundRGBAlphaArray, ("#" + cell.cellId));
        $("#" + cell.cellId).toggleClass("cell_hover", false);
    }

    function processBoardStatus(boardStatus) {
        var alertMessage = 'Finished!!! '
        if (boardStatus === "TIE") {
            alertMessage = alertMessage + 'Final result is a tie';
        } else {
            alertMessage = alertMessage + boardStatus + ' won the game!!';
        }
        return alertMessage;
    }

});