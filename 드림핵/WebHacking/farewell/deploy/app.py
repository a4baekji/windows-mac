from flask import Flask, render_template, request

app = Flask(__name__)

FLAG = 'DH{**flag**}'

answer='**REDACTED**'

@app.route('/')
def index():
    word=request.args.get('answer','')
    if len(word)!=len(answer):
        return render_template('index.html', content="Please input the message.")
    else:
        cnt=0
        for i in range(0,len(answer),1):
            if answer[i]==word[i]:
                cnt+=1
        if cnt==len(answer):
            return render_template('index.html', content=FLAG)
        else:
            return render_template('index.html', content=str(cnt)+' correct!')
    

if __name__ == '__main__':
    app.run(debug=False,host='0.0.0.0',port=5000)