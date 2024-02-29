from flask import Flask, jsonify, request
from flask_cors import CORS

app = Flask(__name__)

@app.route('/login', methods=['POST'])
def login():
    username = request.json.get('username')
    password = request.json.get('password')

    if username == 'admin' and password == 'password':
        response = {
            'success': True,
            'token': 'exampletoken'
        }
    else:
        response = {
            'success': False
        }

    return jsonify(response), 200


if __name__ == '__main__':
    CORS(app)
    app.run(host='0.0.0.0',port=10240)