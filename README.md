# Elasticsearch autocomplete example
Application example that will show hot send events to GA4 using measurement protocol

## Start the stack with docker compose
```
docker-compose up
```

## Elasticsearch index(posts) configuration
```
{
  "settings": {
    "analysis": {
      "analyzer": {
        "autocomplete_analyzer": {
          "tokenizer": "standard",
          "type": "custom",
          "filter": [
            "lowercase",
            "autocomplete_filter"
          ]
        }
      },
      "filter": {
        "autocomplete_filter": {
          "type": "edge_ngram",
          "min_gram": 3,
          "max_gram": 20
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name": {
        "type": "text",
        "analyzer": "autocomplete_analyzer",
        "search_analyzer": "standard"
      },
      "description": {
        "type": "text",
        "analyzer": "autocomplete_analyzer",
        "search_analyzer": "standard"
      },
      "category": {
        "type": "keyword"
      }
    }
  }
}
```
## Elasticsearch test data(posts)
```
[
    {
      "name": "Introduction to React Native",
      "description": "Learn how to build mobile apps with React Native",
      "category": "Software Development"
    },
    {
      "name": "Blockchain for Beginners",
      "description": "An introduction to blockchain technology for those new to the field",
      "category": "Blockchain"
    },
    {
      "name": "Java vs Python: Which is Better for Data Science?",
      "description": "A comparison of Java and Python for data science applications",
      "category": "Software Development"
    },
    {
      "name": "The Basics of Cryptocurrency",
      "description": "An overview of cryptocurrency and how it works",
      "category": "Blockchain"
    },
    {
      "name": "JavaScript Frameworks for Web Development",
      "description": "Discover the most popular JavaScript frameworks for building web applications",
      "category": "Software Development"
    },
    {
      "name": "Understanding Blockchain Consensus Algorithms",
      "description": "An in-depth look at the various consensus algorithms used in blockchain",
      "category": "Blockchain"
    },
    {
      "name": "Python Tips and Tricks",
      "description": "Improve your Python programming skills with these tips and tricks",
      "category": "Software Development"
    },
    {
      "name": "The Top Programming Languages for Data Science",
      "description": "Discover the programming languages that are most commonly used in data science",
      "category": "Software Development"
    },
    {
      "name": "Introduction to Blockchain Technology",
      "description": "Learn the basics of blockchain and how it can be used in various industries",
      "category": "Blockchain"
    },
    {
      "name": "10 Common Mistakes in Java Programming",
      "description": "Avoid these mistakes to improve your Java programming skills",
      "category": "Software Development"
    },
    {
      "name": "Effective Communication Skills",
      "description": "Develop your communication skills to improve your personal and professional relationships",
      "category": "Personal Development"
    },
    {
      "name": "Introduction to Python Programming",
      "description": "Learn the basics of Python programming language and its applications",
      "category": "Software Development"
    },
    {
      "name": "Digital Marketing Strategy",
      "description": "Develop a comprehensive digital marketing strategy for your business",
      "category": "Marketing"
    },
    {
      "name": "Introduction to Machine Learning",
      "description": "Learn the fundamentals of machine learning and how to build predictive models",
      "category": "Data Science"
    },
    {
      "name": "Introduction to Graphic Design",
      "description": "Learn the principles of graphic design and how to use design software",
      "category": "Design"
    },
    {
      "name": "Business Strategy and Planning",
      "description": "Develop and implement an effective business strategy for your organization",
      "category": "Business"
    },
    {
      "name": "Introduction to Photography",
      "description": "Learn the basics of photography, including composition, lighting, and editing",
      "category": "Photography"
    },
    {
      "name": "Human Resource Management",
      "description": "Learn how to effectively manage people in the workplace, including recruitment and performance management",
      "category": "Human Resources"
    },
    {
      "name": "Project Management Fundamentals",
      "description": "Learn the principles of project management, including planning, budgeting, and risk management",
      "category": "Project Management"
    },
    {
      "name": "Introduction to Cloud Computing",
      "description": "Learn the basics of cloud computing and how to use cloud services",
      "category": "Information Technology"
    },
    {
      "name": "Introduction to Data Analysis",
      "description": "Learn the basics of data analysis, including data cleaning, visualization, and statistical analysis",
      "category": "Data Science"
    },
    {
      "name": "Leadership and Management Skills",
      "description": "Develop your leadership and management skills to effectively lead a team or organization",
      "category": "Personal Development"
    }
]
```

## Test cases and http requests to Elasticsearch
### Test 1, search docs by name field and jawa
#### Request:
```
GET http://localhost:8080/api/client/posts/search?field=name&query="jawa"
```
#### Response:
```
[
    {
        "name": "10 Common Mistakes in Java Programming",
        "description": "Avoid these mistakes to improve your Java programming skills",
        "category": "Software Development"
    },
    {
        "name": "JavaScript Frameworks for Web Development",
        "description": "Discover the most popular JavaScript frameworks for building web applications",
        "category": "Software Development"
    },
    {
        "name": "Java vs Python: Which is Better for Data Science?",
        "description": "A comparison of Java and Python for data science applications",
        "category": "Software Development"
    }
]
```

### Test 2, search docs by name field and "jawaskript" 
#### Request:
```
GET http://localhost:8080/api/client/posts/search?field=name&query=jawaskript
```
#### Response:
```
[
    {
        "name": "JavaScript Frameworks for Web Development",
        "description": "Discover the most popular JavaScript frameworks for building web applications",
        "category": "Software Development"
    }
]
```

### Example of real request to Elasticsearch:
```
POST /posts/_search?typed_keys=true {"query":{"match":{"name":{"fuzziness":"AUTO:3,7","query":"daba"}}}}
```
#### Search request to Elasticsearch contains fuzziness property "AUTO:3,7"
