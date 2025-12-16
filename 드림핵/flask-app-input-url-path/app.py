from flask import Flask, render_template

app = Flask(__name__)

@app.route('/welcome/<name>')
def get_welcome_name(name):
    return render_template('result.html', name=name)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=31337)

