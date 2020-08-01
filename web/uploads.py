from flask import Flask
from flask import request, redirect, url_for, render_template
from flask_wtf.file import FileField
from wtforms import SubmitField
from flask_wtf import Form
import sqlite3

app = Flask(__name__)
app.config["SECRET_KEY"] = "secret"


@app.route('/', methods=['GET', 'POST'])
def index():
    form = UploadForm()
    if request.method == 'POST':
        if form.validate_on_submit():
            file_name = form.file.data

            database(name=file_name.filename, data=file_name.read() )

            return render_template("home.html", form=form)
    return render_template("home.html", form=form)


class UploadForm(Form):

    file = FileField()
    submit = SubmitField("Submit")


def database(name, data):
    conn = sqlite3.connect("void.db")
    cursor = conn.cursor()

    cursor.execute(""" CREATE TABLE IF NOT EXISTS uploads ( Name TEXT, File BLOP) """)

    cursor.execute(""" INSERT INTO uploads (Name, File) VALUES (?, ?) """, (name, data))

    conn.commit()
    cursor.close()
    conn.close()


if __name__ == "__main__":
    app.run(debug=True)
    
  