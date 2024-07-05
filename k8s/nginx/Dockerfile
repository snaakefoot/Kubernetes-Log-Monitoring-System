# Use the official Nginx image as the base image
FROM nginx

# Copy your Nginx configuration file to the appropriate location in the container
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80 to allow outside access to the Nginx server
EXPOSE 80

# Command to start Nginx when the container is run
CMD ["nginx", "-g", "daemon off;"]
