# Example of a flawed Terraform configuration

resource "aws_sagemaker_notebook_instance" "bad_example" {
  name          = "bad-example"
  role_arn      = aws_iam_role.sagemaker_role.arn
  instance_type = "ml.t2.medium"
  # Missing: kms_key_id for encryption
}

resource "aws_glue_catalog_database" "bad_example" {
  name = "bad-example"
  # Missing: encryption configuration
}

resource "aws_iam_role" "sagemaker_role" {
  name = "sagemaker_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "sagemaker.amazonaws.com"
        }
      },
    ]
  })

  # Problematic: Grants full access
  inline_policy {
    name   = "bad_policy"
    policy = jsonencode({
      Version   = "2012-10-17"
      Statement = [{
        Action   = "*"
        Resource = "*"
        Effect   = "Allow"
      }]
    })
  }
}

resource "aws_redshift_cluster" "bad_example" {
  cluster_identifier = "bad-example-cluster"
  database_name      = "bad_example"
  master_username    = "admin"
  master_password    = "SuperSecretPassword1!"
  node_type          = "dc2.large"
  cluster_type       = "single-node"
  # Missing: encrypted true with kms_key_id for CMK
}

resource "aws_eks_node_group" "bad_example" {
  cluster_name    = aws_eks_cluster.bad_example.name
  node_group_name = "bad-example-group"
  node_role_arn   = aws_iam_role.eks_node_group_role.arn
  subnet_ids      = aws_subnet.example[*].id

  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }

  # Implicitly allows SSH from anywhere
  remote_access {
    ec2_ssh_key = aws_key_pair.deployer.key_name
    source_security_group_ids = ["0.0.0.0/0"]
  }
}

resource "aws_rds_cluster" "bad_example" {
  cluster_identifier = "bad-example-cluster"
  engine             = "aurora-mysql"
  master_username    = "admin"
  master_password    = "SuperSecretPassword1!"
  # Missing: storage_encrypted true
}

resource "aws_s3_bucket" "bad_example" {
  bucket = "bad-example-bucket"
  acl    = "public-read-write" # Allows public READ and WRITE permissions
}

resource "aws_ec2_instance" "bad_example" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"

  user_data = <<-EOF
              #!/bin/bash
              echo "SECRET_KEY=SuperSecret" > /etc/environment
              EOF
  # Exposes secrets in user data

  associate_public_ip_address = true # Publicly accessible
}

resource "aws_dynamodb_table" "bad_example" {
  name           = "bad-example"
  billing_mode   = "PROVISIONED"
  read_capacity  = 1
  write_capacity = 1
  hash_key       = "Id"

  attribute {
    name = "Id"
    type = "S"
  }

  # Missing: Point-In-Time Recovery configuration
}

# Note: This is just a fraction of the potential issues and services.
