println 'Dumb parser'

def title = 'Hello, world! ' + System.currentTimeMillis()
def content = '''
    Hey! Come merry dol! derry dol! My darling! 
    Light goes the weather-wind and the feathered starling. 
    Down along under Hill, shining in the sunlight, 
    Waiting on the doorstep for the cold starlight, 
    There my pretty lady is, River-woman's daughter, 
    Slender as the willow-wand, clearer than the water. 
    Old Tom Bombadil water-lilies bringing 
    Comes hopping home again. Can you hear him singing? 
    Hey! Come merry dol! derry dol! and merry-o! 
    Goldberry, Goldberry, merry yellow berry-o! 
    Poor old Willow-man, you tuck your roots away! 
    Tom's in a hurry now. Evening will follow day. 
    Tom's going home again water-lilies bringing. 
    Hey! Come derry dol! Can you hear me singing? 
'''

appendNews(title, content)