from flask import Flask, render_template, request, url_for, redirect
import sqlite3
from flask_wtf.file import FileField
from wtforms import SubmitField
from flask_wtf import Form


app = Flask(__name__)
app.config['SECRET_KEY'] = "password1"


@app.route('/')
def index():
    return render_template('home.html')


@app.route('/upload', methods=['GET', 'POST'])
def upload():
    form = UploadForm()
    if request.method == "POST":
        file_name = form.file.data
        name = request.form["name"]
        file = file_name.read()

        conn = sqlite3.connect("void.db")
        cur = conn.cursor()
        cur.execute(""" CREATE TABLE IF NOT EXISTS uploads ( Name TEXT, File BLOP) """)

        cur.execute(""" INSERT INTO uploads (Name, File) VALUES (?, ?) """, (name, file))

        conn.commit()
        cur.close()
        conn.close()
    return render_template('upload.html', form=form)


class UploadForm(Form):
    file = FileField()
    submit = SubmitField("Submit")


@app.route('/update', methods=['GET', 'POST'])
def update():
    conn = sqlite3.connect("void.db")
    cur = conn.cursor()
    conn.row_factory = sqlite3.Row
    cur.execute(""" SELECT * FROM uploads """)
    rows = cur.fetchall()
    return render_template('update.html', rows=rows)


if __name__ == "__main__":
    app.run(debug=True)