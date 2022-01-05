from flask import render_template
import config


# Create the application instance
app = config.connex_app
print(app)
# Read the swagger.yml file to configure the endpoints
app.add_api('swagger.yml')

# Create a URL route in our application for "/"
@app.route('/feu')
def get_fire():
    return "fire!!!!"
    

# If we're running in stand alone mode, run the application
if __name__ == '__main__':
    print("in")
    print(app)
    app.run(debug=True)
