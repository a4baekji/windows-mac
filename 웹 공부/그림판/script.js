const canvas = document.getElementById("drawcanvas");
const ctx = canvas.getContext('2d');
const colorpick = document.getElementById('colorpick');
const clear = document.getElementById('clear');

let isDrawing = false;
let lastX = 0;
let lastY = 0;

function draw(event){
    if(!isDrawing) return; //마우스가 눌려있지 않으면 그리지 않음
    
    ctx.strokeStyle = colorpick.value; //선 색깔 설정
    ctx.lineWidth = 5; //선 굵기 설정
    ctx.lineCap = 'round'; //선 끝 모양 설정
    ctx.beginPath();
    ctx.moveTo(lastX, lastY); //이전 좌표로 이동
    ctx.lineTo(event.offsetX, event.offsetY); //현재 좌표로 선 그림
    ctx.stroke();
    [lastX, lastY] = [event.offsetX, event.offsetY]; //이전 좌표 업데이트
}

canvas.addEventListener('mousedown', (event) => {
    isDrawing = true;
    [lastX, lastY] = [event.offsetX, event.offsetY];
    console.log(lastX, lastY);
    console.log(isDrawing);
});

canvas.addEventListener('mousemove', draw)

canvas.addEventListener('mouseup', () => {
    isDrawing = false
    console.log(isDrawing);
});

canvas.addEventListener('mouseout', () => {
    isDrawing = false;
    console.log(isDrawing);
})

clear.addEventListener('click', () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
})