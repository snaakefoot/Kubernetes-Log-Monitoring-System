# Use a base image with your desired programming language/runtime installed
FROM node:14

# Set the working directory inside the container
WORKDIR /app
COPY package*.json ./
# Copy your application files into the container


# Install dependencies
RUN npm install
COPY . .
# Expose any necessary ports (if applicable)
EXPOSE 3000

# Define the command to run your application
CMD ["npm", "start"]
