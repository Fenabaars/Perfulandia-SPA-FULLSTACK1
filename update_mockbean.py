import os
import glob

def replace_in_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    new_content = content.replace(
        'org.springframework.boot.test.mock.mockito.MockBean',
        'org.springframework.test.context.bean.override.mockito.MockitoBean'
    ).replace('@MockBean', '@MockitoBean')

    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {filepath}")

# Find all test files
for root, dirs, files in os.walk(r'c:\Users\fena\Desktop\perfulandia\Perfulandia-SPA-FULLSTACK1'):
    for file in files:
        if file.endswith('Test.java'):
            replace_in_file(os.path.join(root, file))

print("Done")
