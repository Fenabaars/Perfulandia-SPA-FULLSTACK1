import os
import re

def suppress_null(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Avoid adding multiple times
    if '@SuppressWarnings("null")' in content:
        return

    # Add the annotation before the class declaration
    new_content = re.sub(
        r'(\n(?:public\s+)?class\s+\w+\s*\{)',
        r'\n@SuppressWarnings("null")\1',
        content,
        count=1
    )

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Added SuppressWarnings to {filepath}")

# Find all test files
for root, dirs, files in os.walk(r'c:\Users\fena\Desktop\perfulandia\Perfulandia-SPA-FULLSTACK1'):
    for file in files:
        if file.endswith('Test.java'):
            suppress_null(os.path.join(root, file))

print("Done")
